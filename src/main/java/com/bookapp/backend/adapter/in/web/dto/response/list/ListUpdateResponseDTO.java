package com.bookapp.backend.adapter.in.web.dto.response.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListUpdateResponseDTO {
    private Long id;
    private String title;
    private String description;
    private boolean isPublic;
    private LocalDateTime updatedAt;
}
