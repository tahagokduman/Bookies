package com.bookapp.backend.adapter.in.web.dto.response.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthorUpdateResponseDTO {
    private Long id;
    private String name;
    private String description;
}
