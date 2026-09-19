package com.pulsefit.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulsefit.attendance.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByMemberId(Long memberId);

    List<Attendance> findByFacilityId(Long facilityId);
}