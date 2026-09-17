package com.pulsefit.subscription.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulsefit.subscription.entity.MembershipPlan;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

    List<MembershipPlan> findByStatus(String status);

    boolean existsByPlanName(String planName);
}