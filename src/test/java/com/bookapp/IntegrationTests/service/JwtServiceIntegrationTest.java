package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class JwtServiceIntegrationTest {

    @Autowired
    private IJwtService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "test@example.com", "password",
                null, null, null);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
    }

    @Test
    void testGenerateAndParseToken() {
        String token = jwtService.generateToken(testUser);

        assertNotNull(token, "Token sollte nicht null sein");
        assertFalse(token.isEmpty(), "Token sollte nicht leer sein");

        Claims claims = jwtService.parseToken(token);

        assertNotNull(claims, "Claims sollten nicht null sein");
        assertEquals(testUser.getUsername(), claims.getSubject(), "Username sollte übereinstimmen");

    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.token.string";

        assertThrows(Exception.class, () -> {
            jwtService.parseToken(invalidToken);
        }, "Ungültige Tokens sollten eine Exception werfen");
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        String token = Jwts.builder()
                .setSubject(testUser.getUsername())
                .claim("email", testUser.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000)) // 1 Sekunde
                .compact();

        Thread.sleep(1500);

        assertThrows(Exception.class, () -> {
            jwtService.parseToken(token);
        }, "Abgelaufene Tokens sollten eine Exception werfen");
    }

    @Test
    void testDifferentUsersGenerateDifferentTokens() {
        User anotherUser = new User(2L, "anotheruser", "another@example.com", "password",
                null, null, null);

        String token1 = jwtService.generateToken(testUser);
        String token2 = jwtService.generateToken(anotherUser);

        assertNotEquals(token1, token2, "Verschiedene Benutzer sollten verschiedene Tokens erhalten");

        Claims claims1 = jwtService.parseToken(token1);
        Claims claims2 = jwtService.parseToken(token2);

        assertNotEquals(claims1.getSubject(), claims2.getSubject(), "Subjects sollten unterschiedlich sein");
    }
}