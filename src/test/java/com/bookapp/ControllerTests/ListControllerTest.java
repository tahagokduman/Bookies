package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.ListController;
import com.bookapp.backend.adapter.in.web.dto.request.list.ListCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.list.ListUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.ListWebMapper;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.in.IListService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListControllerTest {

    @Mock
    private IListService listService;

    @Mock
    private ListWebMapper listWebMapper;

    @Mock
    private IBooksInListService booksInListService;

    @Mock
    private BookWebMapper bookWebMapper;

    @InjectMocks
    private ListController listController;
    private List domainList;
    private ListResponseDTO responseDto;

    @BeforeEach
    void setup() {
        domainList = new List(1L, new User(1L), "Meine Liste");
        domainList.setCreatedAt(LocalDateTime.now());
        domainList.setUpdatedAt(LocalDateTime.now());

        responseDto = new ListResponseDTO();
        responseDto.setId(1L);
        responseDto.setTitle("Meine Liste");
        var owner = new com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO();
        owner.setId(1L);
        owner.setUsername("testuser");
        responseDto.setOwner(owner);
    }

    @Test
    void testGetAllLists() {
        when(listService.getAll()).thenReturn(java.util.List.of(domainList));
        when(listWebMapper.toResponseDto(domainList)).thenReturn(responseDto);

        ResponseEntity<java.util.List<ListResponseDTO>> response = listController.getAllLists();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(responseDto.getId(), response.getBody().get(0).getId());

        verify(listService).getAll();
        verify(listWebMapper).toResponseDto(domainList);
    }

    @Test
    void testGetListById_found() {
        when(listService.getById(1L)).thenReturn(Optional.of(domainList));
        when(listWebMapper.toResponseDto(domainList)).thenReturn(responseDto);

        ResponseEntity<ListResponseDTO> response = listController.getListById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto.getId(), response.getBody().getId());

        verify(listService).getById(1L);
        verify(listWebMapper).toResponseDto(domainList);
    }

    @Test
    void testGetListById_notFound() {
        when(listService.getById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            listController.getListById(99L);
        });

        assertTrue(ex.getMessage().contains("List with id 99 not found"));

        verify(listService).getById(99L);
        verifyNoInteractions(listWebMapper);
    }

    @Test
    void testCreateList_success() {
        ListCreateRequestDTO requestDTO = new ListCreateRequestDTO();
        requestDTO.setName("Neue Liste");
        requestDTO.setUserId(1L);

        List newDomainList = new List();
        newDomainList.setName("Neue Liste");
        newDomainList.setUser(new User(1L));

        List savedDomainList = new List(2L, new User(1L), "Neue Liste");

        ListResponseDTO savedResponseDto = new ListResponseDTO();
        savedResponseDto.setId(2L);
        savedResponseDto.setTitle("Neue Liste");
        var owner = new com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO();
        owner.setId(1L);
        owner.setUsername("testuser");
        savedResponseDto.setOwner(owner);

        when(listWebMapper.toDomain(requestDTO)).thenReturn(newDomainList);
        when(listService.create(newDomainList)).thenReturn(savedDomainList);
        when(listWebMapper.toResponseDto(savedDomainList)).thenReturn(savedResponseDto);

        ResponseEntity<ListResponseDTO> response = listController.createList(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedResponseDto.getId(), response.getBody().getId());

        verify(listWebMapper).toDomain(requestDTO);
        verify(listService).create(newDomainList);
        verify(listWebMapper).toResponseDto(savedDomainList);
    }

    @Test
    void testUpdateList_success() {
        ListUpdateRequestDTO updateDTO = new ListUpdateRequestDTO();
        updateDTO.setName("Geänderte Liste");

        List updatedDomainList = new List(1L, new User(1L), "Geänderte Liste");

        when(listService.getById(1L)).thenReturn(Optional.of(domainList));
        when(listService.update(eq(1L), any(List.class))).thenReturn(updatedDomainList);
        when(listWebMapper.toResponseDto(updatedDomainList)).thenReturn(responseDto);

        ResponseEntity<ListResponseDTO> response = listController.updateList(1L, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto.getId(), response.getBody().getId());

        verify(listService).getById(1L);
        verify(listService).update(eq(1L), any(List.class));
        verify(listWebMapper).toResponseDto(updatedDomainList);
    }

    @Test
    void testUpdateList_notFound() {
        ListUpdateRequestDTO updateDTO = new ListUpdateRequestDTO();
        updateDTO.setName("Geänderte Liste");

        when(listService.getById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            listController.updateList(1L, updateDTO);
        });

        assertTrue(ex.getMessage().contains("List with id 1 not found"));

        verify(listService).getById(1L);
        verifyNoMoreInteractions(listService);
        verifyNoInteractions(listWebMapper);
    }

    @Test
    void testDeleteList_success() {
        when(listService.getById(1L)).thenReturn(Optional.of(domainList));
        doNothing().when(listService).delete(1L);

        ResponseEntity<Void> response = listController.deleteList(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(listService).getById(1L);
        verify(listService).delete(1L);
    }

    @Test
    void testDeleteList_notFound() {
        when(listService.getById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            listController.deleteList(1L);
        });

        assertTrue(ex.getMessage().contains("List with id 1 not found"));

        verify(listService).getById(1L);
        verifyNoMoreInteractions(listService);
    }
}
