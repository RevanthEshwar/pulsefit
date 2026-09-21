package com.pulsefit.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.pulsefit.notification.entity.Notification;
import com.pulsefit.notification.repository.NotificationRepository;

import com.pulsefit.notification.exception.NotificationNotFoundException;
import com.pulsefit.notification.client.MemberClient;
import com.pulsefit.notification.dto.MemberResponse;
import feign.FeignException;
import com.pulsefit.notification.exception.MemberNotFoundException;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberClient memberClient;

    public NotificationService(NotificationRepository notificationRepository,
            MemberClient memberClient) {
        this.notificationRepository = notificationRepository;
        this.memberClient = memberClient;
    }

    public Notification createNotification(Notification notification) {

        try {
            memberClient.getMemberById(notification.getMemberId());
        } catch (FeignException.NotFound ex) {
            throw new MemberNotFoundException(
                    "Member not found: " + notification.getMemberId());
        }

        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }

        if (notification.getStatus() == null) {
            notification.setStatus("PENDING");
        }

        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Long notificationId,
            Authentication authentication) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new NotificationNotFoundException("Notification not found"));

        checkUserAccess(notification.getMemberId(), authentication);

        return notification;
    }

    public List<Notification> getNotificationsByMember(Long memberId,
            Authentication authentication) {

        checkUserAccess(memberId, authentication);

        return notificationRepository.findByMemberId(memberId);
    }

    public List<Notification> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatus(status);
    }

    public List<Notification> getNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }

    public Notification markAsSent(Long notificationId) {

        Notification notification = getNotificationByIdForStaff(notificationId);

        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {

        Notification notification = getNotificationByIdForStaff(notificationId);

        notificationRepository.delete(notification);
    }

    private Notification getNotificationByIdForStaff(Long notificationId) {

        return notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new NotificationNotFoundException("Notification not found"));
    }

    private void checkUserAccess(Long notificationMemberId,
            Authentication authentication) {

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        // STAFF and ADMIN can access notifications of any member
        if (role.equals("ROLE_STAFF") || role.equals("ROLE_ADMIN")) {
            return;
        }

        // USER can access only their own notifications
        String username = authentication.getName();

        MemberResponse member;

        try {
            member = memberClient.getMemberByUsername(username);
        } catch (FeignException.NotFound ex) {
            throw new AccessDeniedException("Member profile not found");
        }

        if (!notificationMemberId.equals(member.getMemberId())) {
            throw new AccessDeniedException(
                    "You can access only your own notifications");
        }
    }
}