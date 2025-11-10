package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.user.UserLoginRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserLoginResponseDTO;
import com.bookapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWebLoginMapper {

    private final PasswordEncoder passwordEncoder;

    public User toDomain(UserLoginRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    public UserLoginResponseDTO toLoginResponseDto(User user, String jwtToken) {
        UserLoginResponseDTO dto = new UserLoginResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setToken(jwtToken);
        return dto;
    }
}
