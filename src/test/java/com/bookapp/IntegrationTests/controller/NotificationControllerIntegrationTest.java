package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.response.notification.NotificationResponseDTO;
import com.bookapp.backend.domain.model.notification.Notification;
import com.bookapp.backend.domain.model.notification.NotificationType;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.ports.in.INotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private INotificationService notificationService;

    private Notification testNotification;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .birthdayDate(LocalDate.of(1990, 1, 1))
                .followersCount(new NonNegativeInteger(0))
                .followingCount(new NonNegativeInteger(0))
                .build();

        testNotification = new Notification();
        testNotification.setId(1L);
        testNotification.setSenderId(2L);
        testNotification.setReceiverId(1L);
        testNotification.setType(NotificationType.FOLLOW_USER);
        testNotification.setTargetId("1");
        testNotification.setRead(false);
        testNotification.setCreatedAt(LocalDateTime.now());
        testNotification.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getMyNotifications_WhenUnauthenticated_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isUnauthorized());
    }
}