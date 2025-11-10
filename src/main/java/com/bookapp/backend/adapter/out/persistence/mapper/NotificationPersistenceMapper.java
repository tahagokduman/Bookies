package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.NotificationEntity;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.model.notification.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class NotificationPersistenceMapper {

    public Notification toDomain(NotificationEntity entity) {
        if (entity == null) return null;

        Notification domain = new Notification();
        domain.setId(entity.getId());
        domain.setSenderId(entity.getSenderId());
        domain.setReceiverId(entity.getReceiverId());
        domain.setTargetId(entity.getTargetId());
        domain.setType(NotificationType.valueOf(entity.getType()));
        domain.setRead(entity.isRead());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }

    public NotificationEntity toEntity(Notification domain) {
        if (domain == null) return null;

        NotificationEntity entity = new NotificationEntity();
        entity.setId(domain.getId());
        entity.setSenderId(domain.getSenderId());
        entity.setReceiverId(domain.getReceiverId());
        entity.setTargetId(domain.getTargetId());
        entity.setType(domain.getType().name());
        entity.setRead(domain.isRead());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }
}
