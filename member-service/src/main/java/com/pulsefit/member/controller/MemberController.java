package com.pulsefit.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.member.entity.Member;
import com.pulsefit.member.service.MemberService;

import org.springframework.http.HttpStatus;
import com.pulsefit.member.exception.MemberNotFoundException;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.createMember(member));
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMemberById(memberId));
    }

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

    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.ok("Member deleted successfully");
    }
    
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFound(MemberNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}