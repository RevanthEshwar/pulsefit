package com.pulsefit.member.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

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
        return memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found"));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new MemberNotFoundException("Member not found"));
    }

    public Member updateMember(Long memberId, Member member) {
        Member existingMember = getMemberById(memberId);

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