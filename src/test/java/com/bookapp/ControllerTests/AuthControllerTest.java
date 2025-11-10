package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.AuthController;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserLoginRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserRegisterRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserLoginResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserRegisterResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.UserWebLoginMapper;
import com.bookapp.backend.adapter.in.web.mapper.UserWebRegisterMapper;
import com.bookapp.backend.application.service.AuthUseCases;
import com.bookapp.backend.application.service.JwtUseCases;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private JwtUseCases jwtService;
    private UserWebLoginMapper userWebLoginMapper;
    private UserWebRegisterMapper userWebRegisterMapper;
    private AuthUseCases authService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtUseCases.class);
        userWebLoginMapper = mock(UserWebLoginMapper.class);
        userWebRegisterMapper = mock(UserWebRegisterMapper.class);
        authService = mock(AuthUseCases.class);
        controller = new AuthController(jwtService, userWebLoginMapper, userWebRegisterMapper, authService);
    }

    @Test
    @DisplayName("Login returns token and user info successfully")
    void login_shouldReturnTokenAndUserInfo() {
        // Arrange
        var dto = new UserLoginRequestDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");

        var user = new User(1L);
        user.setUsername("testuser");

        when(userWebLoginMapper.toDomain(dto)).thenReturn(user);
        when(authService.login(user)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");


        ResponseEntity<UserLoginResponseDTO> response = controller.login(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        var body = response.getBody();
        assertEquals(1L, body.getId());
        assertEquals("testuser", body.getUsername());
        assertEquals("fake-jwt-token", body.getToken());

        verify(userWebLoginMapper).toDomain(dto);
        verify(authService).login(user);
        verify(jwtService).generateToken(user);
    }

    @Test
    @DisplayName("Register creates and returns new user")
    void register_shouldReturnCreatedUser() {
        var dto = new UserRegisterRequestDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");

        var user = new User(1L);
        user.setUsername("testuser");

        var responseDto = new UserRegisterResponseDTO();
        responseDto.setId(1L);
        responseDto.setUsername("testuser");

        when(userWebRegisterMapper.toDomain(dto)).thenReturn(user);
        when(authService.register(user)).thenReturn(user);
        when(userWebRegisterMapper.toRegisterResponseDto(user)).thenReturn(responseDto);

        ResponseEntity<UserRegisterResponseDTO> response = controller.register(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        var body = response.getBody();
        assertEquals(1L, body.getId());
        assertEquals("testuser", body.getUsername());

        verify(userWebRegisterMapper).toDomain(dto);
        verify(authService).register(user);
        verify(userWebRegisterMapper).toRegisterResponseDto(user);
    }

    @Test
    @DisplayName("Logout returns no content status")
    void logout_shouldReturnNoContent() {
        ResponseEntity<?> response = controller.logout();

        assertEquals(204, response.getStatusCodeValue());
    }
}
