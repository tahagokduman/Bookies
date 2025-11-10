package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.notification.Notification;

import java.util.List;

public interface INotificationRepository extends IBaseRepository<Notification, Long> {
    List<Notification> findAllByReceiverId(Long receiverId);

    Long findCountOfDidntReadNotifications();

}
