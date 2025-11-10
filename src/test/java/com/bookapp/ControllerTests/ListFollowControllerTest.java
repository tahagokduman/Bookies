package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.ListFollowController;
import com.bookapp.backend.adapter.in.web.dto.request.follow.ListFollowCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.ListFollowResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.ListFollowWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.ListWebMapper;
import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.listfollow.ListFollow;
import com.bookapp.backend.domain.ports.in.IListFollowService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListFollowControllerTest {

    @InjectMocks
    private ListFollowController controller;

    @Mock
    private IListFollowService listFollowService;

    @Mock
    private ListFollowWebMapper listFollowWebMapper;

    @Mock
    private ListWebMapper listWebMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getListFollowById_found_returnsDtoWithLinks() {
        Long userId = 1L;
        Long listId = 2L;
        ListFollowId id = new ListFollowId(userId, listId);

        ListFollow domain = new ListFollow();
        domain.setId(id);

        ListFollowResponseDTO dto = ListFollowResponseDTO.builder()
                .userId(userId)
                .listId(listId)
                .build();

        when(listFollowService.getById(id)).thenReturn(Optional.of(domain));
        when(listFollowWebMapper.toResponseDto(domain)).thenReturn(dto);

        ResponseEntity<ListFollowResponseDTO> response = controller.getListFollowById(userId, listId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
        assertThat(response.getBody().getListId()).isEqualTo(listId);
        assertThat(response.getBody().getLinks()).isNotEmpty();

        verify(listFollowService).getById(id);
        verify(listFollowWebMapper).toResponseDto(domain);
    }

    @Test
    void getListFollowById_notFound_throwsException() {
        Long userId = 1L;
        Long listId = 2L;
        ListFollowId id = new ListFollowId(userId, listId);

        when(listFollowService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.getListFollowById(userId, listId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ListFollow not found");

        verify(listFollowService).getById(id);
        verifyNoInteractions(listFollowWebMapper);
    }

    @Test
    void createListFollow_validRequest_returnsCreatedDtoWithLinks() {
        ListFollowCreateRequestDTO requestDTO = new ListFollowCreateRequestDTO(1L, 2L);

        ListFollow domainToSave = new ListFollow();
        domainToSave.setId(new ListFollowId(1L, 2L));

        ListFollow savedDomain = new ListFollow();
        savedDomain.setId(new ListFollowId(1L, 2L));

        ListFollowResponseDTO responseDTO = ListFollowResponseDTO.builder()
                .userId(1L)
                .listId(2L)
                .build();

        when(listFollowWebMapper.toDomain(requestDTO)).thenReturn(domainToSave);
        when(listFollowService.create(domainToSave)).thenReturn(savedDomain);
        when(listFollowWebMapper.toResponseDto(savedDomain)).thenReturn(responseDTO);

        ResponseEntity<ListFollowResponseDTO> response = controller.createListFollow(requestDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(1L);
        assertThat(response.getBody().getListId()).isEqualTo(2L);
        assertThat(response.getBody().getLinks()).isNotEmpty();

        verify(listFollowWebMapper).toDomain(requestDTO);
        verify(listFollowService).create(domainToSave);
        verify(listFollowWebMapper).toResponseDto(savedDomain);
    }

    @Test
    void deleteListFollowById_existingEntity_returnsNoContent() {
        Long userId = 1L;
        Long listId = 2L;
        ListFollowId id = new ListFollowId(userId, listId);

        ListFollow existing = new ListFollow();
        existing.setId(id);

        when(listFollowService.getById(id)).thenReturn(Optional.of(existing));

        ResponseEntity<Void> response = controller.deleteListFollowById(userId, listId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(listFollowService).getById(id);
        verify(listFollowService).delete(id);
    }

    @Test
    void deleteListFollowById_notFound_throwsException() {
        Long userId = 1L;
        Long listId = 2L;
        ListFollowId id = new ListFollowId(userId, listId);

        when(listFollowService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.deleteListFollowById(userId, listId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ListFollow not found");

        verify(listFollowService).getById(id);
        verify(listFollowService, never()).delete(id);
    }

    @Test
    void isFollowing_returnsTrue() {
        Long userId = 1L;
        Long listId = 2L;

        when(listFollowService.isFollowing(userId, listId)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.isFollowing(userId, listId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isTrue();

        verify(listFollowService).isFollowing(userId, listId);
    }

    @Test
    void getFollowCount_returnsCount() {
        Long listId = 2L;
        Long count = 5L;

        when(listFollowService.countById_ListId(listId)).thenReturn(count);

        ResponseEntity<Long> response = controller.getFollowCount(listId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(count);

        verify(listFollowService).countById_ListId(listId);
    }

    @Test
    void unfollowList_existingFollow_returnsNoContent() {
        ListFollowCreateRequestDTO dto = new ListFollowCreateRequestDTO(1L, 2L);
        ListFollowId id = new ListFollowId(1L, 2L);

        ListFollow existing = new ListFollow();
        existing.setId(id);

        when(listFollowService.getById(id)).thenReturn(Optional.of(existing));

        ResponseEntity<Void> response = controller.unfollowList(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(listFollowService).getById(id);
        verify(listFollowService).delete(id);
    }

    @Test
    void unfollowList_notFound_throwsException() {
        ListFollowCreateRequestDTO dto = new ListFollowCreateRequestDTO(1L, 2L);
        ListFollowId id = new ListFollowId(1L, 2L);

        when(listFollowService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.unfollowList(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ListFollow not found");

        verify(listFollowService).getById(id);
        verify(listFollowService, never()).delete(id);
    }
}
