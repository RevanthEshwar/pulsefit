package com.pulsefit.attendance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import com.pulsefit.attendance.entity.Attendance;
import com.pulsefit.attendance.repository.AttendanceRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pulsefit.attendance.client.MemberClient;
import com.pulsefit.attendance.client.FacilityClient;
import com.pulsefit.attendance.dto.MemberResponse;
import com.pulsefit.attendance.dto.FacilityResponse;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

	@Mock
	private AttendanceRepository attendanceRepository;

	@Mock
	private MemberClient memberClient;

	@Mock
	private FacilityClient facilityClient;

	@InjectMocks
	private AttendanceService attendanceService;

   

	@Test
	void checkIn_shouldSetCheckInTime() {

	    Attendance attendance = new Attendance();
	    attendance.setMemberId(1L);
	    attendance.setFacilityId(1L);

	    when(memberClient.getMemberById(anyLong()))
	            .thenReturn(new MemberResponse());

	    when(facilityClient.getFacilityById(anyLong()))
	            .thenReturn(new FacilityResponse());

	    when(attendanceRepository.save(any(Attendance.class)))
	            .thenAnswer(invocation -> invocation.getArgument(0));

	    Attendance result = attendanceService.checkIn(attendance);

	    assertNotNull(result.getCheckInTime());
	    assertEquals(1L, result.getMemberId());
	    assertEquals(1L, result.getFacilityId());

	    verify(attendanceRepository).save(attendance);
	}

    @Test
    void checkIn_shouldKeepExistingCheckInTime() {

        LocalDateTime time =
                LocalDateTime.of(2026, 9, 19, 10, 0);

        Attendance attendance = new Attendance();
        attendance.setMemberId(1L);
        attendance.setFacilityId(1L);
        attendance.setCheckInTime(time);

        when(attendanceRepository.save(any(Attendance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Attendance result = attendanceService.checkIn(attendance);

        assertEquals(time, result.getCheckInTime());

        verify(attendanceRepository).save(attendance);
    }

    @Test
    void checkOut_shouldSetCheckOutTime() {

        Attendance attendance = new Attendance();
        attendance.setMemberId(1L);
        attendance.setFacilityId(1L);
        attendance.setCheckInTime(
                LocalDateTime.of(2026, 9, 19, 10, 0));

        when(attendanceRepository.findById(1L))
                .thenReturn(Optional.of(attendance));

        when(attendanceRepository.save(any(Attendance.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Attendance result =
                attendanceService.checkOut(1L);

        assertNotNull(result.getCheckOutTime());

        verify(attendanceRepository).findById(1L);
        verify(attendanceRepository).save(attendance);
    }

    @Test
    void getAttendanceById_shouldReturnAttendance() {

        Attendance attendance = new Attendance();
        attendance.setMemberId(1L);
        attendance.setFacilityId(1L);

        when(attendanceRepository.findById(1L))
                .thenReturn(Optional.of(attendance));

        Attendance result =
                attendanceService.getAttendanceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getMemberId());
        assertEquals(1L, result.getFacilityId());

        verify(attendanceRepository).findById(1L);
    }

    @Test
    void getAttendanceById_shouldThrowExceptionWhenNotFound() {

        when(attendanceRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> attendanceService.getAttendanceById(99L));

        assertEquals(
                "Attendance not found",
                exception.getMessage());

        verify(attendanceRepository).findById(99L);
    }

    @Test
    void getAttendanceByMember_shouldReturnAttendance() {

        Attendance attendance = new Attendance();
        attendance.setMemberId(1L);

        when(attendanceRepository.findByMemberId(1L))
                .thenReturn(java.util.List.of(attendance));

        var result =
                attendanceService.getAttendanceByMember(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMemberId());

        verify(attendanceRepository).findByMemberId(1L);
    }

    @Test
    void getAttendanceByFacility_shouldReturnAttendance() {

        Attendance attendance = new Attendance();
        attendance.setFacilityId(1L);

        when(attendanceRepository.findByFacilityId(1L))
                .thenReturn(java.util.List.of(attendance));

        var result =
                attendanceService.getAttendanceByFacility(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFacilityId());

        verify(attendanceRepository).findByFacilityId(1L);
    }

    @Test
    void deleteAttendance_shouldDeleteAttendance() {

        Attendance attendance = new Attendance();

        when(attendanceRepository.findById(1L))
                .thenReturn(Optional.of(attendance));

        attendanceService.deleteAttendance(1L);

        verify(attendanceRepository).delete(attendance);
    }
}