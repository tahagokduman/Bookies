package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.user.UserLoginRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWebMapper {
    private final PasswordEncoder passwordEncoder;

    public User toDomain(UserLoginRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }

    public User updateRequestDTOtoDomain(UserUpdateRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthdayDate(dto.getBirthdayDate());
        return user;
    }

    public UserShortResponseDTO toShortDto(User user) {
        UserShortResponseDTO dto = new UserShortResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBirthdayDate(user.getBirthdayDate());
        dto.setFollowersCount(user.getFollowersCount());
        dto.setFollowingCount(user.getFollowingCount());
        return dto;
    }


    //  TODO : String encodedPassword = passwordEncoder.encode(rawPassword);
    //  TODO : Sifre model User a cevrilirken kesinlikle burada sifrelenmeli!!!
}
