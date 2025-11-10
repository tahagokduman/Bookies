package com.bookapp.backend.adapter.in.web.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentShortResponseDTO {
    private Long id;
    private String content;
}