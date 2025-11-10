package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserRegisterRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserRegisterResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.UserWebRegisterMapper;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserWebRegisterMapperTest {

    private UserWebRegisterMapper mapper;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        mapper = new UserWebRegisterMapper(passwordEncoder);
    }

    @Test
    void toDomain_shouldMapFieldsAndEncodePassword() {
        UserRegisterRequestDTO dto = new UserRegisterRequestDTO();
        dto.setUsername("newUser");
        dto.setEmail("user@example.com");
        dto.setPassword("plainPassword");

        User user = mapper.toDomain(dto);

        assertEquals("newUser", user.getUsername());
        assertEquals("user@example.com", user.getEmail());
        assertNotEquals("plainPassword", user.getPassword());
        assertTrue(passwordEncoder.matches("plainPassword", user.getPassword()));
        assertNull(user.getBirthdayDate());
    }

    @Test
    void toRegisterResponseDto_shouldMapCorrectly() {
        User user = new User(42L);
        user.setUsername("responseUser");

        UserRegisterResponseDTO responseDTO = mapper.toRegisterResponseDto(user);

        assertEquals(42L, responseDTO.getId());
        assertEquals("responseUser", responseDTO.getUsername());
        assertEquals("Registration successful", responseDTO.getMessage());
    }
}
