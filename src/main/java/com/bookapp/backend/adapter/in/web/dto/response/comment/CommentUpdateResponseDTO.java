package com.bookapp.backend.adapter.in.web.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdateResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime updatedAt;
}
