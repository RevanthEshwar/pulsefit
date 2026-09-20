package com.pulsefit.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pulsefit.notification.config.FeignClientConfig;
import com.pulsefit.notification.dto.MemberResponse;

@FeignClient(
        name = "member-service",
        configuration = FeignClientConfig.class
)
public interface MemberClient {

    @GetMapping("/api/members/{memberId}")
    MemberResponse getMemberById(
            @PathVariable("memberId") Long memberId);
}