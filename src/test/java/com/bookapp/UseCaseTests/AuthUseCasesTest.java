package com.bookapp.UseCaseTests;

import com.bookapp.backend.application.service.AuthUseCases;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.*;
import com.bookapp.backend.domain.ports.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCasesTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IUserAvatarRepository userAvatarRepository;

    @Mock
    private IAvatarRepository avatarRepository;

    @InjectMocks
    private AuthUseCases authUseCases;

    private User testUser;
    private Avatar defaultAvatar;

    @BeforeEach
    void setUp() {
        testUser = new User(
                1L,
                "testuser",
                "test@example.com",
                "encodedPassword",
                LocalDate.now(),
                new NonNegativeInteger(0),
                new NonNegativeInteger(0)
        );
        defaultAvatar = new Avatar(1L);
    }

    @Test
    void register_ShouldSuccess_WhenNewUser() {
        User newUser = new User("newuser", "new@example.com", "rawPassword", LocalDate.now());
        User savedUser = new User(
                2L,
                "newuser",
                "new@example.com",
                "rawPassword",
                LocalDate.now(),
                new NonNegativeInteger(0),
                new NonNegativeInteger(0)
        );

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(avatarRepository.findById(1L)).thenReturn(Optional.of(defaultAvatar));
        when(userRepository.findById(2L)).thenReturn(Optional.of(savedUser));

        User result = authUseCases.register(newUser);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("rawPassword", result.getPassword());
        verify(userRepository).save(any(User.class));
        verify(userAvatarRepository).save(any(UserAvatar.class));
    }

    @Test
    void register_ShouldFail_WhenUsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User newUser = new User("testuser", "new@example.com", "password", LocalDate.now());

        assertThrows(DataIntegrityViolationException.class, () -> authUseCases.register(newUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldFail_WhenEmailExists() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        User newUser = new User("newuser", "test@example.com", "password", LocalDate.now());

        assertThrows(DataIntegrityViolationException.class, () -> authUseCases.register(newUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldSuccess_WithValidCredentials() {
        User existingUser = new User(
                1L,
                "testuser",
                "test@example.com",
                "encodedPassword",
                LocalDate.now(),
                new NonNegativeInteger(0),
                new NonNegativeInteger(0)
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        User loginUser = new User();
        loginUser.setUsername("testuser");
        loginUser.setPassword("rawPassword");

        User result = authUseCases.login(loginUser);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void login_ShouldFail_WhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        User loginUser = new User();
        loginUser.setUsername("unknown");
        loginUser.setPassword("password");

        assertThrows(UsernameNotFoundException.class, () -> authUseCases.login(loginUser));
    }

    @Test
    void login_ShouldFail_WithInvalidPassword() {
        User existingUser = new User(
                1L,
                "testuser",
                "test@example.com",
                "encodedPassword",
                LocalDate.now(),
                new NonNegativeInteger(0),
                new NonNegativeInteger(0)
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        User loginUser = new User();
        loginUser.setUsername("testuser");
        loginUser.setPassword("wrongPassword");

        assertThrows(BadCredentialsException.class, () -> authUseCases.login(loginUser));
    }

    @Test
    void createDefaultAvatar_ShouldBeCalled_AfterSuccessfulRegistration() {
        User newUser = new User("newuser", "new@example.com", "password", LocalDate.now());
        User savedUser = new User(
                2L,
                "newuser",
                "new@example.com",
                "password",
                LocalDate.now(),
                new NonNegativeInteger(0),
                new NonNegativeInteger(0)
        );

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(avatarRepository.findById(1L)).thenReturn(Optional.of(defaultAvatar));
        when(userRepository.findById(2L)).thenReturn(Optional.of(savedUser));

        authUseCases.register(newUser);

        verify(userAvatarRepository).save(argThat(userAvatar ->
                userAvatar.getId().getUserId().equals(2L) &&
                        userAvatar.getId().getAvatarId().equals(1L)
        ));
    }
}