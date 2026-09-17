package com.pulsefit.subscription.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.subscription.entity.MembershipPlan;
import com.pulsefit.subscription.service.MembershipPlanService;

@RestController
@RequestMapping("/api/plans")
public class MembershipPlanController {

    private final MembershipPlanService planService;

    public MembershipPlanController(MembershipPlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<MembershipPlan> createPlan(
            @RequestBody MembershipPlan plan) {

        return ResponseEntity.ok(
                planService.createPlan(plan));
    }

    @GetMapping
    public ResponseEntity<List<MembershipPlan>> getAllPlans() {

        return ResponseEntity.ok(
                planService.getAllPlans());
    }

    @GetMapping("/{planId}")
    public ResponseEntity<MembershipPlan> getPlanById(
            @PathVariable Long planId) {

        return ResponseEntity.ok(
                planService.getPlanById(planId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MembershipPlan>> getPlansByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                planService.getPlansByStatus(status));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<MembershipPlan> updatePlan(
            @PathVariable Long planId,
            @RequestBody MembershipPlan plan) {

        return ResponseEntity.ok(
                planService.updatePlan(planId, plan));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlan(
            @PathVariable Long planId) {

        planService.deletePlan(planId);

        return ResponseEntity.ok(
                "Membership plan deleted successfully");
    }
}