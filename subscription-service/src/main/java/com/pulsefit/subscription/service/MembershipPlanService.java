package com.pulsefit.subscription.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pulsefit.subscription.entity.MembershipPlan;
import com.pulsefit.subscription.repository.MembershipPlanRepository;

@Service
public class MembershipPlanService {

    private final MembershipPlanRepository planRepository;

    public MembershipPlanService(MembershipPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public MembershipPlan createPlan(MembershipPlan plan) {
        if (planRepository.existsByPlanName(plan.getPlanName())) {
            throw new RuntimeException("Plan name already exists");
        }

        return planRepository.save(plan);
    }

    public List<MembershipPlan> getAllPlans() {
        return planRepository.findAll();
    }

    public MembershipPlan getPlanById(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() ->
                        new RuntimeException("Membership plan not found"));
    }

    public List<MembershipPlan> getPlansByStatus(String status) {
        return planRepository.findByStatus(status);
    }

    public MembershipPlan updatePlan(Long planId, MembershipPlan plan) {
        MembershipPlan existingPlan = getPlanById(planId);

        existingPlan.setPlanName(plan.getPlanName());
        existingPlan.setDurationMonths(plan.getDurationMonths());
        existingPlan.setPrice(plan.getPrice());
        existingPlan.setAccessType(plan.getAccessType());
        existingPlan.setDescription(plan.getDescription());
        existingPlan.setStatus(plan.getStatus());

        return planRepository.save(existingPlan);
    }

    public void deletePlan(Long planId) {
        MembershipPlan plan = getPlanById(planId);
        planRepository.delete(plan);
    }
}