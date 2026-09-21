package com.pulsefit.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.notification.entity.Notification;
import com.pulsefit.notification.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification) {

        return ResponseEntity.ok(
                notificationService.createNotification(notification));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {

        return ResponseEntity.ok(
                notificationService.getAllNotifications());
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<Notification> getNotificationById(
            @PathVariable Long notificationId,
            Authentication authentication) {

        return ResponseEntity.ok(
                notificationService.getNotificationById(
                        notificationId, authentication));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Notification>> getNotificationsByMember(
            @PathVariable Long memberId,
            Authentication authentication) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByMember(
                        memberId, authentication));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Notification>> getNotificationsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByStatus(status));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> getNotificationsByType(
            @PathVariable String type) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByType(type));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{notificationId}/send")
    public ResponseEntity<Notification> markAsSent(
            @PathVariable Long notificationId) {

        return ResponseEntity.ok(
                notificationService.markAsSent(notificationId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long notificationId) {

        notificationService.deleteNotification(notificationId);

        return ResponseEntity.ok("Notification deleted successfully");
    }
}