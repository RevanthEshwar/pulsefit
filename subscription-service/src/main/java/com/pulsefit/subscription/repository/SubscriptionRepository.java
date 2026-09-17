package com.pulsefit.subscription.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulsefit.subscription.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByMemberId(Long memberId);

    List<Subscription> findByStatus(String status);

    List<Subscription> findByPlanId(Long planId);
}