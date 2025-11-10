package com.bookapp.UseCaseTests;

import com.bookapp.backend.application.service.CommentUseCases;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ICommentRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentUseCasesTest {

    private ICommentRepository commentRepository;
    private IBookRepository bookRepository;
    private IUserRepository userRepository;
    private CommentUseCases commentUseCases;

    private Comment comment;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        commentRepository = mock(ICommentRepository.class);
        bookRepository = mock(IBookRepository.class);
        userRepository = mock(IUserRepository.class);
        commentUseCases = new CommentUseCases(commentRepository, bookRepository, userRepository);

        book = new Book();
        book.setId(1L);

        user = new User();
        user.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setBook(book);
        comment.setUser(user);
        comment.setComment("Nice book");
    }

    @Test
    void testGetAll() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> result = commentUseCases.getAll();

        assertEquals(1, result.size());
        verify(commentRepository).findAll();
    }

    @Test
    void testGetByIdExists() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentUseCases.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Nice book", result.get().getComment());
    }

    @Test
    void testGetByIdNotFound() {
        when(commentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentUseCases.getById(2L));
    }

    @Test
    void testCreateSuccess() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentUseCases.create(comment);

        assertNotNull(result);
        assertEquals("Nice book", result.getComment());
        verify(commentRepository).save(comment);
    }

    @Test
    void testCreateFailsDueToMissingBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentUseCases.create(comment));
    }

    @Test
    void testCreateFailsDueToMissingUser() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentUseCases.create(comment));
    }

    @Test
    void testUpdateSuccess() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.update(comment, 1L)).thenReturn(comment);

        Comment result = commentUseCases.update(1L, comment);

        assertEquals("Nice book", result.getComment());
        verify(commentRepository).update(comment, 1L);
    }

    @Test
    void testUpdateFailsCommentNotFound() {
        when(commentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentUseCases.update(2L, comment));
    }

    @Test
    void testDeleteSuccess() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(1L);

        commentUseCases.delete(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void testDeleteFails() {
        when(commentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentUseCases.delete(2L));
    }

    @Test
    void testGetCommentsPaging() {
        Page<Comment> page = new PageImpl<>(List.of(comment));
        when(commentRepository.findAllByBookId(1L, PageRequest.of(0, 10))).thenReturn(page);

        Page<Comment> result = commentUseCases.getCommentsPaging(0, 10, 1L);

        assertEquals(1, result.getContent().size());
        verify(commentRepository).findAllByBookId(1L, PageRequest.of(0, 10));
    }
}
