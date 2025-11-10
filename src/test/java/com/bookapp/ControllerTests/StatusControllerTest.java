package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.StatusController;
import com.bookapp.backend.adapter.in.web.dto.request.status.StatusRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.StatusWebMapper;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IStatusService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusControllerTest {

    @Mock
    private IStatusService statusService;

    @Mock
    private StatusWebMapper statusWebMapper;

    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStatuses_shouldReturnListOfDTOs() {
        Status status = new Status(1L, "reading", new User(1L));
        StatusResponseDTO dto = new StatusResponseDTO();
        dto.setId(1L);
        dto.setStatus("reading");

        when(statusService.getAll()).thenReturn(List.of(status));
        when(statusWebMapper.toResponseDto(status)).thenReturn(dto);

        ResponseEntity<List<StatusResponseDTO>> response = statusController.getAllStatuses();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getStatus()).isEqualTo("reading");
    }

    @Test
    void getStatusById_shouldReturnDTO() {
        Status status = new Status(1L, "completed", new User(2L));
        StatusResponseDTO dto = new StatusResponseDTO();
        dto.setId(1L);
        dto.setStatus("completed");

        when(statusService.getById(1L)).thenReturn(Optional.of(status));
        when(statusWebMapper.toResponseDto(status)).thenReturn(dto);

        ResponseEntity<StatusResponseDTO> response = statusController.getStatusById(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getStatus()).isEqualTo("completed");
    }

    @Test
    void getStatusById_shouldThrowWhenNotFound() {
        when(statusService.getById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> statusController.getStatusById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void createStatus_shouldReturnCreatedDTO() {
        StatusRequestDTO requestDTO = new StatusRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setStatusName("wishlist");

        Status status = new Status(null, "wishlist", new User(1L));
        Status saved = new Status(5L, "wishlist", new User(1L));
        StatusResponseDTO dto = new StatusResponseDTO();
        dto.setId(5L);
        dto.setStatus("wishlist");

        when(statusWebMapper.toDomain(requestDTO)).thenReturn(status);
        when(statusService.create(status)).thenReturn(saved);
        when(statusWebMapper.toResponseDto(saved)).thenReturn(dto);

        ResponseEntity<StatusResponseDTO> response = statusController.createStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getStatus()).isEqualTo("wishlist");
    }

    @Test
    void updateStatus_shouldReturnUpdatedDTO() {
        StatusRequestDTO requestDTO = new StatusRequestDTO();
        requestDTO.setUserId(2L);
        requestDTO.setStatusName("updated");

        Status updated = new Status(10L, "updated", new User(2L));
        Status saved = new Status(10L, "updated", new User(2L));
        StatusResponseDTO dto = new StatusResponseDTO();
        dto.setId(10L);
        dto.setStatus("updated");

        when(statusWebMapper.toDomain(requestDTO)).thenReturn(updated);
        when(statusService.update(10L, updated)).thenReturn(saved);
        when(statusWebMapper.toResponseDto(saved)).thenReturn(dto);

        ResponseEntity<StatusResponseDTO> response = statusController.updateStatus(10L, requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getStatus()).isEqualTo("updated");
    }

    @Test
    void deleteStatus_shouldCallService() {
        Status status = new Status(1L, "reading", new User(1L));
        when(statusService.getById(1L)).thenReturn(Optional.of(status));

        ResponseEntity<Void> response = statusController.deleteStatus(1L);

        verify(statusService).delete(1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void deleteStatus_shouldThrowWhenNotFound() {
        when(statusService.getById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> statusController.deleteStatus(404L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
