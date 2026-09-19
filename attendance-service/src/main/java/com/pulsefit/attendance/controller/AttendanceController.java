package com.pulsefit.attendance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.attendance.entity.Attendance;
import com.pulsefit.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<Attendance> checkIn(
            @RequestBody Attendance attendance) {

        return ResponseEntity.ok(
                attendanceService.checkIn(attendance));
    }

    @PutMapping("/{attendanceId}/check-out")
    public ResponseEntity<Attendance> checkOut(
            @PathVariable Long attendanceId) {

        return ResponseEntity.ok(
                attendanceService.checkOut(attendanceId));
    }

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance());
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<Attendance> getAttendanceById(
            @PathVariable Long attendanceId) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceById(attendanceId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Attendance>> getAttendanceByMember(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByMember(memberId));
    }

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<Attendance>> getAttendanceByFacility(
            @PathVariable Long facilityId) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByFacility(facilityId));
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable Long attendanceId) {

        attendanceService.deleteAttendance(attendanceId);

        return ResponseEntity.ok(
                "Attendance deleted successfully");
    }
}