package com.bookapp.backend.adapter.in.web.dto.response.status;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import com.bookapp.backend.domain.model.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDTO extends BaseResponseDTO<StatusResponseDTO> {
    private String status;
}
