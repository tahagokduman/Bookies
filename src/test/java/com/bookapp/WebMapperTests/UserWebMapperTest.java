package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserLoginRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.UserWebMapper;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserWebMapperTest {

    private UserWebMapper mapper;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        mapper = new UserWebMapper(passwordEncoder);
    }

    @Test
    void toDomain_shouldMapAndEncodePassword() {
        UserLoginRequestDTO dto = new UserLoginRequestDTO();
        dto.setUsername("testuser");
        dto.setPassword("mysecret");

        User user = mapper.toDomain(dto);

        assertEquals("testuser", user.getUsername());
        assertNotEquals("mysecret", user.getPassword());
        assertTrue(passwordEncoder.matches("mysecret", user.getPassword()));
    }

    @Test
    void updateRequestDTOtoDomain_shouldMapCorrectly() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO();
        dto.setUsername("updatedUser");
        dto.setEmail("mail@test.com");
        dto.setPassword("updatedSecret");
        dto.setBirthdayDate(LocalDate.of(2000, 1, 1));

        User user = mapper.updateRequestDTOtoDomain(dto);

        assertEquals("updatedUser", user.getUsername());
        assertEquals("mail@test.com", user.getEmail());
        assertTrue(passwordEncoder.matches("updatedSecret", user.getPassword()));
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthdayDate());
    }

    @Test
    void toShortDto_shouldMapCorrectly() {
        User user = new User(123L);
        user.setUsername("shorty");

        UserShortResponseDTO dto = mapper.toShortDto(user);

        assertEquals(123L, dto.getId());
        assertEquals("shorty", dto.getUsername());
    }

    @Test
    void toResponseDTO_shouldMapCorrectly() {
        User user = new User(
                999L,
                "longuser",
                "user@mail.com",
                "pw",
                LocalDate.of(1995, 5, 5),
                new NonNegativeInteger(11),
                new NonNegativeInteger(22)
        );

        UserResponseDTO dto = mapper.toResponseDTO(user);

        assertEquals(999L, dto.getId());
        assertEquals("longuser", dto.getUsername());
        assertEquals("user@mail.com", dto.getEmail());
        assertEquals(LocalDate.of(1995, 5, 5), dto.getBirthdayDate());
        assertEquals(new NonNegativeInteger(11), dto.getFollowersCount());
        assertEquals(new NonNegativeInteger(22), dto.getFollowingCount());
    }
}
