package com.pulsefit.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pulsefit.notification.entity.Notification;
import com.pulsefit.notification.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification(
                1L,
                "EXPIRY",
                "Your membership will expire in 7 days.",
                "PENDING",
                LocalDateTime.now(),
                null
        );

        notification.setNotificationId(1L);
    }

    @Test
    void createNotificationTest() {

        when(notificationRepository.save(notification))
                .thenReturn(notification);

        Notification result =
                notificationService.createNotification(notification);

        assertNotNull(result);
        assertEquals(1L, result.getNotificationId());
        assertEquals("EXPIRY", result.getType());
        assertEquals("PENDING", result.getStatus());

        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void getAllNotificationsTest() {

        when(notificationRepository.findAll())
                .thenReturn(Arrays.asList(notification));

        List<Notification> result =
                notificationService.getAllNotifications();

        assertEquals(1, result.size());
        assertEquals("EXPIRY", result.get(0).getType());

        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void getNotificationByIdTest() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        Notification result =
                notificationService.getNotificationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNotificationId());

        verify(notificationRepository, times(1)).findById(1L);
    }

    @Test
    void getNotificationByIdNotFoundTest() {

        when(notificationRepository.findById(999L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.getNotificationById(999L)
        );

        assertEquals("Notification not found", exception.getMessage());

        verify(notificationRepository, times(1)).findById(999L);
    }

    @Test
    void getNotificationsByMemberTest() {

        when(notificationRepository.findByMemberId(1L))
                .thenReturn(Arrays.asList(notification));

        List<Notification> result =
                notificationService.getNotificationsByMember(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMemberId());

        verify(notificationRepository, times(1))
                .findByMemberId(1L);
    }

    @Test
    void getNotificationsByStatusTest() {

        when(notificationRepository.findByStatus("PENDING"))
                .thenReturn(Arrays.asList(notification));

        List<Notification> result =
                notificationService.getNotificationsByStatus("PENDING");

        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());

        verify(notificationRepository, times(1))
                .findByStatus("PENDING");
    }

    @Test
    void getNotificationsByTypeTest() {

        when(notificationRepository.findByType("EXPIRY"))
                .thenReturn(Arrays.asList(notification));

        List<Notification> result =
                notificationService.getNotificationsByType("EXPIRY");

        assertEquals(1, result.size());
        assertEquals("EXPIRY", result.get(0).getType());

        verify(notificationRepository, times(1))
                .findByType("EXPIRY");
    }

    @Test
    void markAsSentTest() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        when(notificationRepository.save(notification))
                .thenReturn(notification);

        Notification result =
                notificationService.markAsSent(1L);

        assertEquals("SENT", result.getStatus());
        assertNotNull(result.getSentAt());

        verify(notificationRepository, times(1))
                .findById(1L);

        verify(notificationRepository, times(1))
                .save(notification);
    }

    @Test
    void deleteNotificationTest() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        notificationService.deleteNotification(1L);

        verify(notificationRepository, times(1))
                .findById(1L);

        verify(notificationRepository, times(1))
                .delete(notification);
    }
}