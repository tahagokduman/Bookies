package com.bookapp.UseCaseTests;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.bookapp.backend.application.service.NotificationUseCases;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.ports.out.INotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

class NotificationUseCasesTest {

    @Mock
    private INotificationRepository repository;

    @InjectMocks
    private NotificationUseCases service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnNotification() {
        Notification notification = new Notification();
        when(repository.save(notification)).thenReturn(notification);

        Notification result = service.create(notification);

        verify(repository).save(notification);
        assertThat(result).isSameAs(notification);
    }

    @Test
    void findAllByReceiverId_shouldReturnList() {
        Long receiverId = 1L;
        List<Notification> list = List.of(new Notification(), new Notification());
        when(repository.findAllByReceiverId(receiverId)).thenReturn(list);

        List<Notification> result = service.findAllByReceiverId(receiverId);

        verify(repository).findAllByReceiverId(receiverId);
        assertThat(result).hasSize(2);
    }

    @Test
    void markAsRead_shouldSetReadTrueAndSave() {
        Notification notification = new Notification();
        notification.setRead(false);
        when(repository.findById(5L)).thenReturn(Optional.of(notification));
        when(repository.save(notification)).thenReturn(notification);

        service.markAsRead(5L);

        assertThat(notification.isRead()).isTrue();
        verify(repository).save(notification);
    }
}
