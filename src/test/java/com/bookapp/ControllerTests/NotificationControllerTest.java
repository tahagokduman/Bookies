package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.NotificationController;
import com.bookapp.backend.adapter.in.web.dto.response.notification.NotificationResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.NotificationWebMapper;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.ports.in.INotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.CollectionModel;


import java.util.List;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @InjectMocks
    private NotificationController controller;

    @Mock
    private INotificationService notificationService;

    @Mock
    private NotificationWebMapper mapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMyNotifications_returnsCollectionModelWithDtosAndLinks() {
        Long userId = 1L;

        Notification notification1 = new Notification(10L, userId, null, "target1", false);
        Notification notification2 = new Notification(20L, userId, null, "target2", true);

        List<Notification> notifications = List.of(notification1, notification2);

        NotificationResponseDTO dto1 = new NotificationResponseDTO(10L, userId, null, "target1", false);
        NotificationResponseDTO dto2 = new NotificationResponseDTO(20L, userId, null, "target2", true);

        when(notificationService.findAllByReceiverId(userId)).thenReturn(notifications);
        when(mapper.toResponseDTO(notification1)).thenReturn(dto1);
        when(mapper.toResponseDTO(notification2)).thenReturn(dto2);

        CollectionModel<NotificationResponseDTO> result = controller.getMyNotifications(userId);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2).containsExactly(dto1, dto2);

        dto1.getLinks().forEach(link -> assertThat(link.getRel().value()).isEqualTo("self"));
        dto2.getLinks().forEach(link -> assertThat(link.getRel().value()).isEqualTo("self"));

        verify(notificationService).findAllByReceiverId(userId);
        verify(mapper).toResponseDTO(notification1);
        verify(mapper).toResponseDTO(notification2);
    }

    @Test
    void markAsRead_callsServiceMarkAsRead() {
        Long notificationId = 5L;

        doNothing().when(notificationService).markAsRead(notificationId);

        controller.markAsRead(notificationId);

        verify(notificationService).markAsRead(notificationId);
    }
}
