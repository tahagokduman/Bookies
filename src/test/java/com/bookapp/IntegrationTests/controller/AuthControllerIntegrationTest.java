package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.user.UserLoginRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserRegisterRequestDTO;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IUserAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserAvatarRepository userAvatarRepository;

    @Autowired
    private IAvatarRepository avatarRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@bookapp.com";
    private static final String TEST_PASSWORD = "Test1234!";
    private static String authToken;



    @Test
    @Order(1)
    @DisplayName("POST /api/auth/register - User erfolgreich registrieren")
    void registerUser_shouldReturn201() throws Exception {
        UserRegisterRequestDTO request = new UserRegisterRequestDTO();
        request.setUsername(TEST_USERNAME);
        request.setEmail(TEST_EMAIL);
        request.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME));
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/auth/login - Erfolgreicher Login")
    void login_shouldReturn200WithToken() throws Exception {
        User user = new User();
        user.setUsername(TEST_USERNAME);
        user.setEmail(TEST_EMAIL);
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        userRepository.save(user);

        UserLoginRequestDTO request = new UserLoginRequestDTO();
        request.setUsername(TEST_USERNAME); // Nicht die Email, sondern den Benutzernamen verwenden
        request.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
    @Test
    @Order(3)
    @DisplayName("POST /api/auth/login - Falsches Passwort")
    void loginWithWrongPassword_shouldReturn401() throws Exception {
        User user = new User();
        user.setUsername(TEST_USERNAME);
        user.setEmail(TEST_EMAIL);
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        userRepository.save(user);

        UserLoginRequestDTO request = new UserLoginRequestDTO();
        request.setUsername(TEST_USERNAME);
        request.setPassword("FalschesPasswort");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}