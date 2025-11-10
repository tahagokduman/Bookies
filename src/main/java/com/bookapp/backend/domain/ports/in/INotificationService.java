package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.notification.Notification;

import java.util.List;

public interface INotificationService extends IBaseService<Notification, Long> {

    void createFollowUserNotification(Long senderId, Long receiverId);

    void createFollowListNotification(Long senderId, Long receiverId, String listId);

    void createLikeCommentNotification(Long senderId, Long receiverId, String commentId);

    List<Notification> findAllByReceiverId(Long receiverId);

    void markAsRead(Long id);

    void deleteNotification(Long id, Long userId);

    Long findCountOfDidntReadNotifications();
}
