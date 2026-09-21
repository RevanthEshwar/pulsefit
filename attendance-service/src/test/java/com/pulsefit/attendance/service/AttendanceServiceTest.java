package com.pulsefit.attendance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pulsefit.attendance.client.FacilityClient;
import com.pulsefit.attendance.client.MemberClient;
import com.pulsefit.attendance.dto.FacilityResponse;
import com.pulsefit.attendance.dto.MemberResponse;
import com.pulsefit.attendance.entity.Attendance;
import com.pulsefit.attendance.repository.AttendanceRepository;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private MemberClient memberClient;

    @Mock
    private FacilityClient facilityClient;

    @Mock
    private Authentication authentication;

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

        when(memberClient.getMemberById(anyLong()))
                .thenReturn(new MemberResponse());

        when(facilityClient.getFacilityById(anyLong()))
                .thenReturn(new FacilityResponse());

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

        Attendance result = attendanceService.checkOut(1L);

        assertNotNull(result.getCheckOutTime());

        verify(attendanceRepository).findById(1L);
        verify(attendanceRepository).save(attendance);
    }
    
    @Test
    void checkOut_shouldThrowExceptionWhenAlreadyCheckedOut() {

        Attendance attendance = new Attendance();

        attendance.setMemberId(1L);
        attendance.setFacilityId(1L);
        attendance.setCheckInTime(
                LocalDateTime.of(2026, 9, 19, 10, 0));

        attendance.setCheckOutTime(
                LocalDateTime.of(2026, 9, 19, 18, 0));

        when(attendanceRepository.findById(1L))
                .thenReturn(Optional.of(attendance));

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> attendanceService.checkOut(1L));

        assertEquals(
                "Attendance has already been checked out",
                exception.getMessage());

        verify(attendanceRepository)
                .findById(1L);

        verify(attendanceRepository, never())
                .save(any(Attendance.class));
    }

    @Test
    void getAttendanceById_shouldReturnAttendance() {

        Attendance attendance = new Attendance();

        attendance.setMemberId(1L);
        attendance.setFacilityId(1L);

        when(attendanceRepository.findById(1L))
                .thenReturn(Optional.of(attendance));

        when(authentication.getAuthorities())
                .thenAnswer(invocation ->
                        java.util.Collections.singleton(
                                new SimpleGrantedAuthority("ROLE_USER")));

        when(authentication.getName())
                .thenReturn("memberuser");

        MemberResponse memberResponse =
                new MemberResponse();

        memberResponse.setMemberId(1L);

        when(memberClient.getMemberByUsername("memberuser"))
                .thenReturn(memberResponse);

        Attendance result =
                attendanceService.getAttendanceById(
                        1L,
                        authentication);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getMemberId());

        assertEquals(
                1L,
                result.getFacilityId());

        verify(attendanceRepository)
                .findById(1L);

        verify(memberClient)
                .getMemberByUsername("memberuser");
    }

    @Test
    void getAttendanceById_shouldThrowExceptionWhenNotFound() {

        when(attendanceRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> attendanceService.getAttendanceById(
                                99L,
                                authentication));

        assertEquals(
                "Attendance not found",
                exception.getMessage());

        verify(attendanceRepository)
                .findById(99L);
    }

    @Test
    void getAttendanceByMember_shouldReturnAttendance() {

        Attendance attendance = new Attendance();

        attendance.setMemberId(1L);

        when(authentication.getAuthorities())
                .thenAnswer(invocation ->
                        java.util.Collections.singleton(
                                new SimpleGrantedAuthority("ROLE_USER")));

        when(authentication.getName())
                .thenReturn("memberuser");

        MemberResponse memberResponse =
                new MemberResponse();

        memberResponse.setMemberId(1L);

        when(memberClient.getMemberByUsername("memberuser"))
                .thenReturn(memberResponse);

        when(attendanceRepository.findByMemberId(1L))
                .thenReturn(List.of(attendance));

        List<Attendance> result =
                attendanceService.getAttendanceByMember(
                        1L,
                        authentication);

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).getMemberId());

        verify(attendanceRepository)
                .findByMemberId(1L);

        verify(memberClient)
                .getMemberByUsername("memberuser");
    }

    @Test
    void getAttendanceByFacility_shouldReturnAttendance() {

        Attendance attendance = new Attendance();

        attendance.setFacilityId(1L);

        when(attendanceRepository.findByFacilityId(1L))
                .thenReturn(List.of(attendance));

        List<Attendance> result =
                attendanceService.getAttendanceByFacility(1L);

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).getFacilityId());

        verify(attendanceRepository)
                .findByFacilityId(1L);
    }

    @Test
    void deleteAttendance_shouldDeleteAttendance() {

        Attendance attendance = new Attendance();

        when(attendanceRepository.findById(1L))
                .thenReturn(Optional.of(attendance));

        attendanceService.deleteAttendance(1L);

        verify(attendanceRepository)
                .delete(attendance);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}