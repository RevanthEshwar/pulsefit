package com.pulsefit.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pulsefit.notification.entity.Notification;
import com.pulsefit.notification.repository.NotificationRepository;

import com.pulsefit.notification.exception.NotificationNotFoundException;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {

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

    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
        		.orElseThrow(() -> new NotificationNotFoundException("Notification not found"));    }

    public List<Notification> getNotificationsByMember(Long memberId) {
        return notificationRepository.findByMemberId(memberId);
    }

    public List<Notification> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatus(status);
    }

    public List<Notification> getNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }

    public Notification markAsSent(Long notificationId) {

        Notification notification = getNotificationById(notificationId);

        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {

        Notification notification = getNotificationById(notificationId);

        notificationRepository.delete(notification);
    }
}