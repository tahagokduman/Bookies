package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.adapter.out.persistence.repository.IJpaUserRepository;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIntegrationTest {

    @Autowired
    private IUserService userService;


    @Autowired
    private IJpaUserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_and_fetchById() {
        User user = User.builder()
                .username("john")
                .email("john@example.com")
                .password("pass123")
                .followersCount(new NonNegativeInteger(0))
                .followingCount(new NonNegativeInteger(0))
                .build();

        User created = userService.create(user);
        assertNotNull(created.getId());

        Optional<User> fetched = userService.getById(created.getId());
        assertTrue(fetched.isPresent());
        assertEquals("john", fetched.get().getUsername());
    }
}
