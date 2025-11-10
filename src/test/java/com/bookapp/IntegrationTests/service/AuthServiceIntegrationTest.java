package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IAuthService;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceIntegrationTest {

    @Autowired
    private IAuthService authService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("newuser@example.com");
        user.setPassword(passwordEncoder.encode("securepass"));

        User registered = authService.register(user);

        assertNotNull(registered.getId());
        assertEquals("newuser", registered.getUsername());
        assertEquals("newuser@example.com", registered.getEmail());

        Optional<User> found = userRepository.findByUsername("newuser");
        assertTrue(found.isPresent());
        assertEquals("newuser@example.com", found.get().getEmail());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        User user = new User();
        user.setUsername("loginuser");
        user.setEmail("loginuser@example.com");
        user.setPassword(passwordEncoder.encode("mypassword"));

        authService.register(user);

        User loginAttempt = new User();
        loginAttempt.setUsername("loginuser");
        loginAttempt.setPassword("mypassword");

        User loggedIn = authService.login(loginAttempt);

        assertNotNull(loggedIn);
        assertEquals("loginuser", loggedIn.getUsername());
    }
}
