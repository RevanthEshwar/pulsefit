package com.pulsefit.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulsefit.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberId(Long memberId);

    List<Notification> findByStatus(String status);

    List<Notification> findByType(String type);
}