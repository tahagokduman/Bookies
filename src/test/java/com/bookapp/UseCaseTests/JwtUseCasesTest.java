package com.bookapp.UseCaseTests;

import com.bookapp.backend.application.service.JwtUseCases;
import com.bookapp.backend.domain.model.user.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class JwtUseCasesTest {

    private JwtUseCases jwtUseCases;

    @BeforeEach
    void setUp() {
        jwtUseCases = new JwtUseCases();
        jwtUseCases.setSecretKey("123456789012345678901234567890121231231412412412412312312313124");
    }

    @Test
    void testGenerateAndParseToken() {
        User user = new User();
        user.setId(42L);
        user.setUsername("testuser");
        user.setEmail("email@test.com");
        user.setPassword("password");
        user.setBirthdayDate(LocalDate.of(1990, 1, 1));

        String token = jwtUseCases.generateToken(user);
        assertNotNull(token, "Token should not be null");

        Claims claims = jwtUseCases.parseToken(token);
        assertNotNull(claims, "Claims should not be null");


        assertEquals(user.getUsername(), claims.getSubject(), "Subject (username) mismatch");
        assertEquals(user.getId().intValue(), claims.get("userId", Integer.class), "User ID mismatch");

        assertNotNull(claims.getIssuedAt(), "IssuedAt should not be null");
        assertNotNull(claims.getExpiration(), "Expiration should not be null");
    }
}
