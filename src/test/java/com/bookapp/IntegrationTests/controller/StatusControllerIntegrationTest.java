package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.status.StatusRequestDTO;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IStatusRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StatusControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IStatusRepository statusRepository;

    private User testUser;
    private User otherUser;
    private Status testStatus;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@test.com");
        otherUser.setPassword("password123");
        otherUser = userRepository.save(otherUser);

        testStatus = new Status();
        testStatus.setStatus("Reading");
        testStatus = statusRepository.save(testStatus);
    }


    @Test
    @DisplayName("GET /api/statuses ohne Authentifizierung - Soll 401 zurückgeben")
    void getAllStatuses_WhenUnauthenticated_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/statuses"))
                .andExpect(status().isUnauthorized());
    }
}