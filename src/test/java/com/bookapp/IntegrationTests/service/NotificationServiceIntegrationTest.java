package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.model.notification.NotificationType;
import com.bookapp.backend.domain.ports.in.INotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NotificationServiceIntegrationTest {

    @Autowired
    private INotificationService notificationService;

    private Long testSenderId;
    private Long testReceiverId;

    @BeforeEach
    void setUp() {
        testSenderId = 1L;
        testReceiverId = 2L;
    }

    @Test
    public void testCreateAndGetNotification() {
        Notification notification = new Notification(
                testSenderId,
                testReceiverId,
                NotificationType.FOLLOW_USER,
                "123",
                false
        );

        Notification created = notificationService.create(notification);
        assertNotNull(created.getId());

        Notification found = notificationService.getById(created.getId()).orElseThrow();
        assertThat(found.getType()).isEqualTo(NotificationType.FOLLOW_USER);
    }

    @Test
    public void testCreateFollowUserNotification() {
        notificationService.createFollowUserNotification(testSenderId, testReceiverId);

        List<Notification> notifications = notificationService.findAllByReceiverId(testReceiverId);
        assertThat(notifications).hasSize(1);
        Notification firstNotification = notifications.get(0);
        assertThat(firstNotification.getType()).isEqualTo(NotificationType.FOLLOW_USER);
        assertThat(firstNotification.getSenderId()).isEqualTo(testSenderId);
        assertThat(firstNotification.getTargetId()).isEqualTo(String.valueOf(testSenderId));
    }

    @Test
    public void testCreateFollowListNotification() {
        String listId = "5";
        notificationService.createFollowListNotification(testSenderId, testReceiverId, listId);

        List<Notification> notifications = notificationService.findAllByReceiverId(testReceiverId);
        assertThat(notifications).hasSize(1);
        Notification firstNotification = notifications.get(0);
        assertThat(firstNotification.getType()).isEqualTo(NotificationType.FOLLOW_LIST);
        assertThat(firstNotification.getTargetId()).isEqualTo(listId);
    }

    @Test
    public void testCreateLikeCommentNotification() {
        String commentId = "10";
        notificationService.createLikeCommentNotification(testSenderId, testReceiverId, commentId);

        List<Notification> notifications = notificationService.findAllByReceiverId(testReceiverId);
        assertThat(notifications).hasSize(1);
        Notification firstNotification = notifications.get(0);
        assertThat(firstNotification.getType()).isEqualTo(NotificationType.LIKE_COMMENT);
        assertThat(firstNotification.getTargetId()).isEqualTo(commentId);
    }


}