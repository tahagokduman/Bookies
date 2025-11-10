package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.user.UserRegisterRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserRegisterResponseDTO;
import com.bookapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWebRegisterMapper {

    private final PasswordEncoder passwordEncoder;

    public User toDomain(UserRegisterRequestDTO dto) {
        User user = new User(
                dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                null
        );
        return user;
    }

    public UserRegisterResponseDTO toRegisterResponseDto(User user) {
        UserRegisterResponseDTO dto = new UserRegisterResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setMessage("Registration successful");

        return dto;
    }
}
