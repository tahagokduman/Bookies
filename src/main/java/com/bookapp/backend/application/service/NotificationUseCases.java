package com.bookapp.backend.application.service;

import com.bookapp.backend.adapter.out.persistence.entity.NotificationEntity;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.model.notification.NotificationType;
import com.bookapp.backend.domain.ports.in.INotificationService;
import com.bookapp.backend.domain.ports.out.INotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationUseCases implements INotificationService {

    private final INotificationRepository notificationRepository;

    @Override
    public Notification create(Notification notification) {
        setTimestampsForCreate(notification);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll().reversed();
    }

    @Override
    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification update(Long id, Notification notification) {
        notification.setId(id);
        notification.setUpdatedAt(LocalDateTime.now());
        return notificationRepository.update(notification, id);
    }

    @Override
    public List<Notification> findAllByReceiverId(Long receiverId) {
        return notificationRepository.findAllByReceiverId(receiverId);
    }

    @Override
    public void createFollowUserNotification(Long senderId, Long receiverId) {
        Notification n = new Notification(senderId, receiverId, NotificationType.FOLLOW_USER, senderId.toString(), false);
        setTimestampsForCreate(n);
        notificationRepository.save(n);
    }

    @Override
    public void createFollowListNotification(Long senderId, Long receiverId, String listId) {
        Notification n = new Notification(senderId, receiverId, NotificationType.FOLLOW_LIST, listId, false);
        setTimestampsForCreate(n);
        notificationRepository.save(n);
    }

    @Override
    public void createLikeCommentNotification(Long senderId, Long receiverId, String commentId) {
        Notification n = new Notification(senderId, receiverId, NotificationType.LIKE_COMMENT, commentId, false);
        setTimestampsForCreate(n);
        notificationRepository.save(n);
    }

    @Override
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            n.setUpdatedAt(LocalDateTime.now());
            notificationRepository.save(n);
        });
    }

    private void setTimestampsForCreate(Notification n) {
        LocalDateTime now = LocalDateTime.now();
        n.setCreatedAt(now);
        n.setUpdatedAt(now);
    }

    @Override
    public void deleteNotification(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benachrichtigung nicht gefunden"));

        if (!notification.getReceiverId().equals(userId)) {
            throw new AccessDeniedException("Sie dürfen diese Benachrichtigung nicht löschen");
        }

        notificationRepository.deleteById(id);
    }

    @Override
    public Long findCountOfDidntReadNotifications() {
        return notificationRepository.findCountOfDidntReadNotifications();
    }
}