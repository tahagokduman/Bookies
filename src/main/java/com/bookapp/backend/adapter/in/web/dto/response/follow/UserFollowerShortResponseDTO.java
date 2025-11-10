package com.bookapp.backend.adapter.in.web.dto.response.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollowerShortResponseDTO {
    private Long id;
    private String username;
    private LocalDateTime followedAt;
}
