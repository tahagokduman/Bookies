package com.bookapp.WebMapperTests;

import com.bookapp.backend.adapter.in.web.dto.request.status.StatusRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusShortResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.StatusWebMapper;
import com.bookapp.backend.domain.model.status.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StatusWebMapperTest {

    private final StatusWebMapper mapper = new StatusWebMapper();

    @Test
    void toDomain_shouldMapCorrectly() {
        StatusRequestDTO dto = new StatusRequestDTO();
        dto.setId(1L);
        dto.setStatusName("Gelesen");

        Status status = mapper.toDomain(dto);

        assertNotNull(status);
        assertEquals(1L, status.getId());
        assertEquals("Gelesen", status.getStatus());
    }

    @Test
    void toShortDto_shouldMapCorrectly() {
        Status status = new Status();
        status.setId(5L);
        status.setStatus("Will lesen");

        StatusShortResponseDTO dto = mapper.toShortDto(status);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals("Will lesen", dto.getStatus());
    }

    @Test
    void toResponseDto_shouldMapCorrectly() {
        Status status = new Status();
        status.setId(10L);
        status.setStatus("Lesend");
        status.setCreatedAt(LocalDateTime.now());
        status.setUpdatedAt(LocalDateTime.now());

        StatusResponseDTO dto = mapper.toResponseDto(status);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Lesend", dto.getStatus());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }
}
