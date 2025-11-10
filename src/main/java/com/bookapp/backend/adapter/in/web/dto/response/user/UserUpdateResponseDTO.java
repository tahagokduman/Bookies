package com.bookapp.backend.adapter.in.web.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateResponseDTO {
    private Long id;
    private String username;
    private String email;
    private boolean approved;
    private LocalDateTime updatedAt;
}
