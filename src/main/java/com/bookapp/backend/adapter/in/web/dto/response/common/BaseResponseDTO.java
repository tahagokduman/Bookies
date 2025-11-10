package com.bookapp.backend.adapter.in.web.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public abstract class BaseResponseDTO<T extends BaseResponseDTO<T>> extends RepresentationModel<T> {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
