package com.pulsefit.member.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pulsefit.member.entity.Member;
import com.pulsefit.member.exception.MemberNotFoundException;
import com.pulsefit.member.repository.MemberRepository;
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        member.setCreatedAt(LocalDateTime.now());

        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        boolean isStaff = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isStaff && !isAdmin &&
                !member.getUsername().equals(username)) {

        	throw new AccessDeniedException("Access denied");
        }

        return member;
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found"));
    }

    public Member updateMember(Long memberId, Member member) {

        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        boolean isStaff = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isStaff && !isAdmin &&
                !existingMember.getUsername().equals(username)) {

            throw new AccessDeniedException("Access denied");
        }

        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setContact(member.getContact());
        existingMember.setAddress(member.getAddress());

        return memberRepository.save(existingMember);
    }
    public void deleteMember(Long memberId) {
        Member member = getMemberById(memberId);
        memberRepository.delete(member);
    }
}