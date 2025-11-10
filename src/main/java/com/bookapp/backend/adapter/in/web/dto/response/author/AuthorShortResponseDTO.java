package com.bookapp.backend.adapter.in.web.dto.response.author;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuthorShortResponseDTO {
    private Long id;
    private String name;
}
