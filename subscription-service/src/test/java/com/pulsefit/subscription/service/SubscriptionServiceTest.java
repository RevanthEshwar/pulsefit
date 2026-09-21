package com.pulsefit.subscription.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pulsefit.subscription.entity.Subscription;
import com.pulsefit.subscription.entity.MembershipPlan;
import com.pulsefit.subscription.repository.SubscriptionRepository;
import com.pulsefit.subscription.repository.MembershipPlanRepository;
import com.pulsefit.subscription.client.MemberClient;
import com.pulsefit.subscription.client.NotificationClient;
import com.pulsefit.subscription.dto.MemberResponse;
import com.pulsefit.subscription.dto.NotificationRequest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private MemberClient memberClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private MembershipPlanRepository membershipPlanRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void createSubscription_shouldSetDefaultValues() {

        Subscription subscription = new Subscription();

        subscription.setMemberId(1L);
        subscription.setPlanId(1L);

        when(memberClient.getMemberById(anyLong()))
                .thenReturn(new MemberResponse());

        MembershipPlan plan = new MembershipPlan();

        plan.setPlanId(1L);
        plan.setDurationMonths(6);

        when(membershipPlanRepository.findById(1L))
                .thenReturn(Optional.of(plan));

        doNothing().when(notificationClient)
                .createNotification(any(NotificationRequest.class));

        when(subscriptionRepository.save(any(Subscription.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Subscription result =
                subscriptionService.createSubscription(subscription);

        assertNotNull(result.getStartDate());

        assertNotNull(result.getEndDate());

        assertEquals(
                result.getStartDate().plusMonths(6),
                result.getEndDate());

        assertEquals(
                "ACTIVE",
                result.getStatus());

        assertEquals(
                "PENDING",
                result.getPaymentStatus());

        verify(memberClient)
                .getMemberById(1L);

        verify(membershipPlanRepository)
                .findById(1L);

        verify(subscriptionRepository)
                .save(subscription);
    }

    @Test
    void createSubscription_shouldThrowExceptionWhenPlanNotFound() {

        Subscription subscription = new Subscription();

        subscription.setMemberId(1L);
        subscription.setPlanId(999L);

        when(memberClient.getMemberById(1L))
                .thenReturn(new MemberResponse());

        when(membershipPlanRepository.findById(999L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> subscriptionService
                                .createSubscription(subscription));

        assertEquals(
                "Membership plan not found: 999",
                exception.getMessage());

        verify(membershipPlanRepository)
                .findById(999L);

        verify(subscriptionRepository, never())
                .save(any(Subscription.class));
    }

    @Test
    void getSubscriptionById_shouldReturnSubscription() {

        Subscription subscription = new Subscription();

        subscription.setMemberId(1L);
        subscription.setPlanId(1L);

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(subscription));

        Subscription result =
                subscriptionService.getSubscriptionById(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getMemberId());

        assertEquals(
                1L,
                result.getPlanId());

        verify(subscriptionRepository)
                .findById(1L);
    }

    @Test
    void getSubscriptionById_shouldThrowExceptionWhenNotFound() {

        when(subscriptionRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> subscriptionService
                                .getSubscriptionById(99L));

        assertEquals(
                "Subscription not found",
                exception.getMessage());

        verify(subscriptionRepository)
                .findById(99L);
    }

    @Test
    void getSubscriptionsByMember_shouldReturnSubscriptions() {

        Subscription subscription = new Subscription();

        subscription.setMemberId(1L);

        when(subscriptionRepository.findByMemberId(1L))
                .thenReturn(
                        java.util.List.of(subscription));

        var result =
                subscriptionService
                        .getSubscriptionsByMember(1L);

        assertEquals(
                1,
                result.size());

        assertEquals(
                1L,
                result.get(0).getMemberId());

        verify(subscriptionRepository)
                .findByMemberId(1L);
    }

    @Test
    void getSubscriptionsByPlan_shouldReturnSubscriptions() {

        Subscription subscription = new Subscription();

        subscription.setPlanId(1L);

        when(subscriptionRepository.findByPlanId(1L))
                .thenReturn(
                        java.util.List.of(subscription));

        var result =
                subscriptionService
                        .getSubscriptionsByPlan(1L);

        assertEquals(
                1,
                result.size());

        assertEquals(
                1L,
                result.get(0).getPlanId());

        verify(subscriptionRepository)
                .findByPlanId(1L);
    }

    @Test
    void updateSubscription_shouldUpdatePersonalTrainer() {

        Subscription existing = new Subscription();

        existing.setMemberId(1L);
        existing.setPlanId(1L);
        existing.setPersonalTrainer(false);

        Subscription updated = new Subscription();

        updated.setMemberId(1L);
        updated.setPlanId(1L);
        updated.setStartDate(
                LocalDate.of(2026, 9, 17));
        updated.setEndDate(
                LocalDate.of(2027, 3, 17));
        updated.setStatus("ACTIVE");
        updated.setPersonalTrainer(true);
        updated.setPaymentStatus("PAID");

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(subscriptionRepository.save(
                any(Subscription.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0));

        Subscription result =
                subscriptionService
                        .updateSubscription(
                                1L,
                                updated);

        assertTrue(
                result.getPersonalTrainer());

        assertEquals(
                "PAID",
                result.getPaymentStatus());

        verify(subscriptionRepository)
                .save(existing);
    }

    @Test
    void deleteSubscription_shouldDeleteSubscription() {

        Subscription subscription =
                new Subscription();

        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(subscription));

        subscriptionService.deleteSubscription(1L);

        verify(subscriptionRepository)
                .delete(subscription);
    }
}