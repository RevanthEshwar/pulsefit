package com.pulsefit.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.pulsefit.member.entity.Member;
import com.pulsefit.member.exception.MemberNotFoundException;
import com.pulsefit.member.repository.MemberRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void getMemberById_shouldReturnMember() {

        Member member = new Member();
        member.setMemberId(1L);
        member.setName("Revanth");
        member.setEmail("revanth@example.com");

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        Member result = memberService.getMemberById(1L);

        assertEquals("Revanth", result.getName());
        assertEquals("revanth@example.com", result.getEmail());

        verify(memberRepository).findById(1L);
    }

    @Test
    void getMemberById_shouldThrowExceptionWhenNotFound() {

        when(memberRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MemberNotFoundException.class,
                () -> memberService.getMemberById(1L)
        );

        verify(memberRepository).findById(1L);
    }
}