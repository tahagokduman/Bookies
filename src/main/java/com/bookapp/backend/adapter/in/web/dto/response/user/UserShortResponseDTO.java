package com.bookapp.backend.adapter.in.web.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortResponseDTO {
    private Long id;
    private String username;
}
