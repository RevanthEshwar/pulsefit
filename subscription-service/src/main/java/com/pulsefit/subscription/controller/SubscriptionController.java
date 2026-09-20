package com.pulsefit.subscription.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.subscription.entity.Subscription;
import com.pulsefit.subscription.exception.SubscriptionNotFoundException;
import com.pulsefit.subscription.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(
            SubscriptionService subscriptionService) {

        this.subscriptionService = subscriptionService;
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Subscription> createSubscription(
            @RequestBody Subscription subscription) {

        return ResponseEntity.ok(
                subscriptionService.createSubscription(subscription));
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {

        return ResponseEntity.ok(
                subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<Subscription> getSubscriptionById(
            @PathVariable Long subscriptionId) {

        return ResponseEntity.ok(
                subscriptionService.getSubscriptionById(subscriptionId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByMember(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(
                subscriptionService.getSubscriptionsByMember(memberId));
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByPlan(
            @PathVariable Long planId) {

        return ResponseEntity.ok(
                subscriptionService.getSubscriptionsByPlan(planId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                subscriptionService.getSubscriptionsByStatus(status));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/{subscriptionId}")
    public ResponseEntity<Subscription> updateSubscription(
            @PathVariable Long subscriptionId,
            @RequestBody Subscription subscription) {

        return ResponseEntity.ok(
                subscriptionService.updateSubscription(
                        subscriptionId, subscription));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<String> deleteSubscription(
            @PathVariable Long subscriptionId) {

        subscriptionService.deleteSubscription(subscriptionId);

        return ResponseEntity.ok(
                "Subscription deleted successfully");
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<String> handleSubscriptionNotFound(
            SubscriptionNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(
            AccessDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
}