package com.pulsefit.attendance.service;

import java.time.LocalDateTime;
import java.util.List;

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

	public AttendanceService(AttendanceRepository attendanceRepository,
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

        Attendance attendance = getAttendanceById(attendanceId);

        attendance.setCheckOutTime(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(Long attendanceId) {

        return attendanceRepository.findById(attendanceId)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Attendance not found"));
    }

    public List<Attendance> getAttendanceByMember(Long memberId) {
        return attendanceRepository.findByMemberId(memberId);
    }

    public List<Attendance> getAttendanceByFacility(Long facilityId) {
        return attendanceRepository.findByFacilityId(facilityId);
    }

    public void deleteAttendance(Long attendanceId) {

        Attendance attendance = getAttendanceById(attendanceId);

        attendanceRepository.delete(attendance);
    }
}