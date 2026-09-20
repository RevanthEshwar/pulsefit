package com.pulsefit.attendance.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pulsefit.attendance.dto.FacilityResponse;

@FeignClient(name = "facility-service")
public interface FacilityClient {

    @GetMapping("/api/facilities/{facilityId}")
    FacilityResponse getFacilityById(@PathVariable("facilityId") Long facilityId);
}