package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserLoginRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserLoginResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.UserWebLoginMapper;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserWebLoginMapperTest {

    private UserWebLoginMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserWebLoginMapper(new BCryptPasswordEncoder());
    }

    @Test
    void toDomain_shouldMapCorrectly() {
        UserLoginRequestDTO dto = new UserLoginRequestDTO();
        dto.setUsername("testuser");
        dto.setPassword("secret");

        User user = mapper.toDomain(dto);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void toLoginResponseDto_shouldMapCorrectly() {
        User user = new User();
        user.setId(42L);
        user.setUsername("tester");

        String token = "jwt-token-example";

        UserLoginResponseDTO dto = mapper.toLoginResponseDto(user, token);

        assertNotNull(dto);
        assertEquals(42L, dto.getId());
        assertEquals("tester", dto.getUsername());
        assertEquals("jwt-token-example", dto.getToken());
    }
}
