package com.bookapp.SecurityTests;

import com.bookapp.backend.application.config.CommentSecurity;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.ICommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentSecurityTest {

    @Mock
    private ICommentService commentService;

    @InjectMocks
    private CommentSecurity commentSecurity;

    @Test
    void isCommentOwner_shouldReturnTrue_ifUserIsOwner() {
        Long commentId = 1L;
        Long userId = 10L;
        Comment comment = mock(Comment.class);
        User user = mock(User.class);

        when(commentService.getById(commentId)).thenReturn(Optional.of(comment));
        when(comment.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(userId);

        boolean result = commentSecurity.isCommentOwner(commentId, userId);

        assertTrue(result);
        verify(commentService, times(1)).getById(commentId);
    }

    @Test
    void isCommentOwner_shouldReturnFalse_ifUserIsNotOwner() {
        Long commentId = 1L;
        Long userId = 10L;
        Comment comment = mock(Comment.class);
        User user = mock(User.class);

        when(commentService.getById(commentId)).thenReturn(Optional.of(comment));
        when(comment.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(999L);

        boolean result = commentSecurity.isCommentOwner(commentId, userId);

        assertFalse(result);
        verify(commentService, times(1)).getById(commentId);
    }

    @Test
    void isCommentOwner_shouldReturnFalse_ifCommentNotFound() {
        when(commentService.getById(anyLong())).thenReturn(Optional.empty());

        boolean result = commentSecurity.isCommentOwner(1L, 10L);

        assertFalse(result);
        verify(commentService, times(1)).getById(1L);
    }
}
