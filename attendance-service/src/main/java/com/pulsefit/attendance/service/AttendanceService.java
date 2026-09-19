package com.pulsefit.attendance.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pulsefit.attendance.entity.Attendance;
import com.pulsefit.attendance.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance checkIn(Attendance attendance) {

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
                        new RuntimeException("Attendance not found"));
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