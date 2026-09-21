package com.pulsefit.subscription.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import feign.FeignException;

import com.pulsefit.subscription.client.MemberClient;
import com.pulsefit.subscription.client.NotificationClient;
import com.pulsefit.subscription.dto.NotificationRequest;
import com.pulsefit.subscription.entity.Subscription;
import com.pulsefit.subscription.exception.SubscriptionNotFoundException;
import com.pulsefit.subscription.repository.SubscriptionRepository;
import com.pulsefit.subscription.repository.MembershipPlanRepository;
import com.pulsefit.subscription.entity.MembershipPlan;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final MembershipPlanRepository membershipPlanRepository;

    private final MemberClient memberClient;

    private final NotificationClient notificationClient;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            MembershipPlanRepository membershipPlanRepository,
            MemberClient memberClient,
            NotificationClient notificationClient) {

        this.subscriptionRepository = subscriptionRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.memberClient = memberClient;
        this.notificationClient = notificationClient;
    }

    public Subscription createSubscription(
            Subscription subscription) {

        // Validate member
        memberClient.getMemberById(subscription.getMemberId());

        // Validate membership plan
        MembershipPlan plan = membershipPlanRepository
                .findById(subscription.getPlanId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Membership plan not found: "
                                        + subscription.getPlanId()));

        // Set start date if not provided
        if (subscription.getStartDate() == null) {

            subscription.setStartDate(LocalDate.now());
        }

        // Calculate end date using plan duration
        subscription.setEndDate(
                subscription.getStartDate()
                        .plusMonths(plan.getDurationMonths()));

        // Set default status
        if (subscription.getStatus() == null) {

            subscription.setStatus("ACTIVE");
        }

        // Set default payment status
        if (subscription.getPaymentStatus() == null) {

            subscription.setPaymentStatus("PENDING");
        }

        Subscription savedSubscription =
                subscriptionRepository.save(subscription);

        // Create notification
        NotificationRequest notification =
                new NotificationRequest();

        notification.setMemberId(
                subscription.getMemberId());

        notification.setType(
                "SUBSCRIPTION_CREATED");

        notification.setMessage(
                "Your PulseFit subscription has been created successfully.");

        notification.setStatus("PENDING");

        try {

            notificationClient.createNotification(
                    notification);

        } catch (FeignException ex) {

            System.out.println(
                    "Subscription created, but notification could not be sent: "
                            + ex.getMessage());
        }

        return savedSubscription;
    }

    public List<Subscription> getAllSubscriptions() {

        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionById(
            Long subscriptionId) {

        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() ->
                        new SubscriptionNotFoundException(
                                "Subscription not found"));
    }

    public List<Subscription> getSubscriptionsByMember(
            Long memberId) {

        return subscriptionRepository.findByMemberId(memberId);
    }

    public List<Subscription> getSubscriptionsByPlan(
            Long planId) {

        return subscriptionRepository.findByPlanId(planId);
    }

    public List<Subscription> getSubscriptionsByStatus(
            String status) {

        return subscriptionRepository.findByStatus(status);
    }

    public Subscription updateSubscription(
            Long subscriptionId,
            Subscription subscription) {

        Subscription existingSubscription =
                getSubscriptionById(subscriptionId);

        existingSubscription.setMemberId(
                subscription.getMemberId());

        existingSubscription.setPlanId(
                subscription.getPlanId());

        existingSubscription.setStartDate(
                subscription.getStartDate());

        existingSubscription.setEndDate(
                subscription.getEndDate());

        existingSubscription.setStatus(
                subscription.getStatus());

        existingSubscription.setPersonalTrainer(
                subscription.getPersonalTrainer());

        existingSubscription.setPaymentStatus(
                subscription.getPaymentStatus());

        return subscriptionRepository.save(
                existingSubscription);
    }

    public void deleteSubscription(
            Long subscriptionId) {

        Subscription subscription =
                getSubscriptionById(subscriptionId);

        subscriptionRepository.delete(subscription);
    }
}