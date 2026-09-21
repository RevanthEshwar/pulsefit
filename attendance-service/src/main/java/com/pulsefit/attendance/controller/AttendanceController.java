package com.pulsefit.attendance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.attendance.entity.Attendance;
import com.pulsefit.attendance.exception.AttendanceNotFoundException;
import com.pulsefit.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping("/check-in")
    public ResponseEntity<Attendance> checkIn(
            @RequestBody Attendance attendance) {

        return ResponseEntity.ok(
                attendanceService.checkIn(attendance));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/{attendanceId}/check-out")
    public ResponseEntity<Attendance> checkOut(
            @PathVariable Long attendanceId) {

        return ResponseEntity.ok(
                attendanceService.checkOut(attendanceId));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance());
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<Attendance> getAttendanceById(
            @PathVariable Long attendanceId,
            Authentication authentication) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceById(
                        attendanceId,
                        authentication));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Attendance>> getAttendanceByMember(
            @PathVariable Long memberId,
            Authentication authentication) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByMember(
                        memberId,
                        authentication));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<Attendance>> getAttendanceByFacility(
            @PathVariable Long facilityId) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByFacility(facilityId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable Long attendanceId) {

        attendanceService.deleteAttendance(attendanceId);

        return ResponseEntity.ok(
                "Attendance deleted successfully");
    }

    @ExceptionHandler(AttendanceNotFoundException.class)
    public ResponseEntity<String> handleAttendanceNotFound(
            AttendanceNotFoundException ex) {

        return ResponseEntity.status(404)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity.status(403)
                .body("Access Denied");
    }
}