package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.CommentController;
import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.comment.CommentResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.CommentWebMapper;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.ICommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Mock
    private ICommentService commentService;

    @Mock
    private CommentWebMapper commentWebMapper;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCommentById_whenFound_returnsOk() {
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);

        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setId(id);

        when(commentService.getById(id)).thenReturn(Optional.of(comment));
        when(commentWebMapper.toResponseDto(comment)).thenReturn(responseDTO);

        ResponseEntity<CommentResponseDTO> response = commentController.getCommentById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
        verify(commentService).getById(id);
        verify(commentWebMapper).toResponseDto(comment);
    }

    @Test
    void getCommentById_whenNotFound_throwsEntityNotFoundException() {
        Long id = 1L;
        when(commentService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentController.getCommentById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Comment with id " + id + " not found");

        verify(commentService).getById(id);
        verifyNoInteractions(commentWebMapper);
    }

    @Test
    void getCommentsByBookId_returnsPagedComments() {
        Long bookId = 5L;
        int page = 0;
        int size = 2;

        Comment comment1 = new Comment();
        comment1.setId(1L);
        Comment comment2 = new Comment();
        comment2.setId(2L);

        List<Comment> comments = List.of(comment1, comment2);
        Page<Comment> commentPage = new PageImpl<>(comments, PageRequest.of(page, size), comments.size());

        CommentResponseDTO dto1 = new CommentResponseDTO();
        dto1.setId(1L);
        CommentResponseDTO dto2 = new CommentResponseDTO();
        dto2.setId(2L);

        when(commentService.getCommentsPaging(page, size, bookId)).thenReturn(commentPage);
        when(commentWebMapper.toResponseDto(comment1)).thenReturn(dto1);
        when(commentWebMapper.toResponseDto(comment2)).thenReturn(dto2);

        ResponseEntity<CollectionModel<CommentResponseDTO>> response = commentController.getCommentsByBookId(bookId, page, size);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(2);

        verify(commentService).getCommentsPaging(page, size, bookId);
        verify(commentWebMapper, times(2)).toResponseDto(any(Comment.class));
    }

    @Test
    void createComment_returnsCreated() {
        CommentCreateRequestDTO createRequestDTO = new CommentCreateRequestDTO();
        createRequestDTO.setBookId(10L);
        createRequestDTO.setUserId(20L);
        createRequestDTO.setScore(5);
        createRequestDTO.setComment("Nice book");

        User userDetails = new User();
        userDetails.setId(20L);

        Comment domainComment = new Comment();
        Comment savedComment = new Comment();
        savedComment.setId(1L);

        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setId(1L);

        when(commentWebMapper.toDomain(createRequestDTO)).thenReturn(domainComment);
        when(commentService.create(any(Comment.class))).thenReturn(savedComment);
        when(commentWebMapper.toResponseDto(savedComment)).thenReturn(responseDTO);

        ResponseEntity<CommentResponseDTO> response = commentController.createComment(createRequestDTO, userDetails);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(commentService).create(any(Comment.class));
    }

    @Test
    void updateCommentById_returnsOk() {
        Long id = 1L;
        CommentUpdateRequestDTO updateDTO = new CommentUpdateRequestDTO();
        updateDTO.setScore(7);
        updateDTO.setComment("Updated comment");

        Comment domainComment = new Comment();
        domainComment.setId(id);

        Comment updatedComment = new Comment();
        updatedComment.setId(id);

        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setId(id);

        when(commentWebMapper.toDomain(updateDTO)).thenReturn(domainComment);
        when(commentService.update(id, domainComment)).thenReturn(updatedComment);
        when(commentWebMapper.toResponseDto(updatedComment)).thenReturn(responseDTO);

        ResponseEntity<CommentResponseDTO> response = commentController.updateCommentById(id, updateDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);

        verify(commentService).update(id, domainComment);
    }

    @Test
    void deleteCommentById_whenExists_returnsNoContent() {
        Long id = 1L;
        Comment comment = new Comment();
        comment.setId(id);

        when(commentService.getById(id)).thenReturn(Optional.of(comment));
        doNothing().when(commentService).delete(id);

        ResponseEntity<?> response = commentController.deleteCommentById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(commentService).getById(id);
        verify(commentService).delete(id);
    }

    @Test
    void deleteCommentById_whenNotFound_throwsEntityNotFoundException() {
        Long id = 1L;

        when(commentService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentController.deleteCommentById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Comment with id " + id + " not found");

        verify(commentService).getById(id);
        verify(commentService, never()).delete(id);
    }

    @Test
    void handleEntityNotFoundException_returnsNotFound() {
        String errorMessage = "Not found";
        EntityNotFoundException ex = new EntityNotFoundException(errorMessage);

        ResponseEntity<String> response = commentController.handleEntityNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }
}
