package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.status.StatusRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusShortResponseDTO;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class StatusWebMapper {

    public Status toDomain(StatusRequestDTO dto) {
        Status status = new Status();
        status.setId(dto.getId());
        status.setStatus(dto.getStatusName());
        return status;
    }

    public StatusShortResponseDTO toShortDto(Status status) {
        StatusShortResponseDTO dto = new StatusShortResponseDTO();
        dto.setId(status.getId());
        dto.setStatus(status.getStatus());
        return dto;
    }


    public StatusResponseDTO toResponseDto(Status entity) {
        StatusResponseDTO dto = new StatusResponseDTO();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
