package com.pulsefit.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.member.entity.Member;
import com.pulsefit.member.exception.MemberNotFoundException;
import com.pulsefit.member.service.MemberService;
import org.springframework.security.core.Authentication;

import org.springframework.security.access.AccessDeniedException;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Member> createMember(
            @RequestBody Member member,
            Authentication authentication) {

        member.setUsername(authentication.getName());

        return ResponseEntity.ok(memberService.createMember(member));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMemberById(memberId));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<Member> updateMember(
            @PathVariable Long memberId,
            @RequestBody Member member) {

        return ResponseEntity.ok(
                memberService.updateMember(memberId, member));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.ok("Member deleted successfully");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFound(
            MemberNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(
            AccessDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
}