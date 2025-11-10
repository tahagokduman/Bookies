package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.response.notification.NotificationResponseDTO;
import com.bookapp.backend.domain.model.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationWebMapper {

    public NotificationResponseDTO toResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());
        dto.setSenderId(notification.getSenderId());
        dto.setReceiverId(notification.getReceiverId());
        dto.setType(notification.getType().name());
        dto.setTargetId(notification.getTargetId());
        dto.setRead(notification.isRead());
        return dto;
    }
}
