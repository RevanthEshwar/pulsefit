package com.pulsefit.attendance.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pulsefit.attendance.config.FeignClientConfig;
import com.pulsefit.attendance.dto.MemberResponse;

@FeignClient(
        name = "member-service",
        configuration = FeignClientConfig.class
)
public interface MemberClient {

    @GetMapping("/api/members/{memberId}")
    MemberResponse getMemberById(
            @PathVariable("memberId") Long memberId);
}