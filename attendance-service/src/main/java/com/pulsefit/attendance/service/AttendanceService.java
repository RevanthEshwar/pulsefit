package com.pulsefit.attendance.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.pulsefit.attendance.entity.Attendance;
import com.pulsefit.attendance.repository.AttendanceRepository;
import com.pulsefit.attendance.client.MemberClient;
import com.pulsefit.attendance.dto.MemberResponse;
import com.pulsefit.attendance.exception.MemberNotFoundException;
import com.pulsefit.attendance.client.FacilityClient;
import com.pulsefit.attendance.exception.FacilityNotFoundException;
import com.pulsefit.attendance.exception.AttendanceNotFoundException;

import feign.FeignException;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final MemberClient memberClient;

    private final FacilityClient facilityClient;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            MemberClient memberClient,
            FacilityClient facilityClient) {

        this.attendanceRepository = attendanceRepository;
        this.memberClient = memberClient;
        this.facilityClient = facilityClient;
    }

    public Attendance checkIn(Attendance attendance) {

        try {

            memberClient.getMemberById(attendance.getMemberId());

        } catch (FeignException.NotFound ex) {

            throw new MemberNotFoundException(
                    "Member not found: " + attendance.getMemberId());

        }

        try {

            facilityClient.getFacilityById(attendance.getFacilityId());

        } catch (FeignException.NotFound ex) {

            throw new FacilityNotFoundException(
                    "Facility not found: " + attendance.getFacilityId());

        }

        if (attendance.getCheckInTime() == null) {

            attendance.setCheckInTime(LocalDateTime.now());

        }

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(Long attendanceId) {

        Attendance attendance = getAttendanceByIdForStaff(attendanceId);

        // Prevent checking out an attendance that is already checked out
        if (attendance.getCheckOutTime() != null) {

            throw new IllegalStateException(
                    "Attendance has already been checked out");

        }

        attendance.setCheckOutTime(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance() {

        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(
            Long attendanceId,
            Authentication authentication) {

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Attendance not found"));

        checkUserAccess(attendance.getMemberId(), authentication);

        return attendance;
    }

    public List<Attendance> getAttendanceByMember(
            Long memberId,
            Authentication authentication) {

        checkUserAccess(memberId, authentication);

        return attendanceRepository.findByMemberId(memberId);
    }

    public List<Attendance> getAttendanceByFacility(Long facilityId) {

        return attendanceRepository.findByFacilityId(facilityId);
    }

    public void deleteAttendance(Long attendanceId) {

        Attendance attendance = getAttendanceByIdForStaff(attendanceId);

        attendanceRepository.delete(attendance);
    }

    private Attendance getAttendanceByIdForStaff(Long attendanceId) {

        return attendanceRepository.findById(attendanceId)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Attendance not found"));
    }

    private void checkUserAccess(
            Long attendanceMemberId,
            Authentication authentication) {

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_STAFF") ||
                role.equals("ROLE_ADMIN")) {

            return;
        }

        String username = authentication.getName();

        MemberResponse member;

        try {

            member = memberClient.getMemberByUsername(username);

        } catch (FeignException.NotFound ex) {

            throw new AccessDeniedException(
                    "Member profile not found");
        }

        if (!attendanceMemberId.equals(member.getMemberId())) {

            throw new AccessDeniedException(
                    "You can access only your own attendance");
        }
    }
}