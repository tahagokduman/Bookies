package com.bookapp.backend.adapter.in.web.dto.response.status;

import com.bookapp.backend.domain.model.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusShortResponseDTO {
    private Long id;
    private String status;
}
