package com.bookapp.WebMapperTests;
import static org.assertj.core.api.Assertions.*;

import com.bookapp.backend.adapter.in.web.dto.response.notification.NotificationResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.NotificationWebMapper;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.model.notification.NotificationType;
import org.junit.jupiter.api.Test;

class NotificationWebMapperTest {

    private NotificationWebMapper mapper = new NotificationWebMapper();

    @Test
    void toResponseDTO_shouldMapAllFields() {
        Notification notification = new Notification(1L, 2L, NotificationType.FOLLOW_USER, "targetId", true);
        notification.setId(99L);

        NotificationResponseDTO dto = mapper.toResponseDTO(notification);

        assertThat(dto.getId()).isEqualTo(99L);
        assertThat(dto.getSenderId()).isEqualTo(1L);
        assertThat(dto.getReceiverId()).isEqualTo(2L);
        assertThat(dto.getType()).isEqualTo("FOLLOW_USER");
        assertThat(dto.getTargetId()).isEqualTo("targetId");
        assertThat(dto.isRead()).isTrue();
    }
}
