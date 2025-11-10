package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.LikedCommentController;
import com.bookapp.backend.adapter.in.web.dto.request.comment.LikedCommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.mapper.CommentWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.LikedCommentWebMapper;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;
import com.bookapp.backend.domain.ports.in.ILikedCommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LikedCommentControllerTest {

    private ILikedCommentService service;
    private LikedCommentWebMapper likedCommentMapper;
    private CommentWebMapper commentMapper;
    private LikedCommentController controller;

    @BeforeEach
    void setUp() {
        service = mock(ILikedCommentService.class);
        likedCommentMapper = new LikedCommentWebMapper();
        commentMapper = mock(CommentWebMapper.class); // CommentWebMapper mocklandı
        controller = new LikedCommentController(service, likedCommentMapper, commentMapper);
    }

    @Test
    void testLikeComment_Success() {
        LikedCommentCreateRequestDTO dto = new LikedCommentCreateRequestDTO(1L, 2L);
        LikedComment likedComment = likedCommentMapper.toDomain(dto);

        when(service.create(any())).thenReturn(likedComment);

        ResponseEntity<Void> response = controller.likeComment(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service).create(any());
    }

    @Test
    void testIsLiked_True() {
        when(service.isCommentLiked(1L, 2L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.isLiked(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testIsLiked_False() {
        when(service.isCommentLiked(1L, 2L)).thenReturn(false);

        ResponseEntity<Boolean> response = controller.isLiked(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void testCountLikes() {
        when(service.countByCommentId(2L)).thenReturn(5L);

        ResponseEntity<Long> response = controller.countLikes(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    @Test
    void testUnlike() {
        doNothing().when(service).delete(any());

        ResponseEntity<Void> response = controller.unlike(1L, 2L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).delete(any(LikedCommentId.class));
    }
}
