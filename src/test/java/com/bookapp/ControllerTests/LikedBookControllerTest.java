package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.LikedBookController;
import com.bookapp.backend.adapter.in.web.dto.request.follow.LikedBookRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.LikedBookResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.LikedBookWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.ILikedBookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikedBookControllerTest {

    private ILikedBookService service;
    private LikedBookWebMapper likedBookMapper;
    private BookWebMapper bookMapper;
    private LikedBookController controller;

    @BeforeEach
    void setUp() {
        service = mock(ILikedBookService.class);
        likedBookMapper = new LikedBookWebMapper();
        bookMapper = new BookWebMapper();
        controller = new LikedBookController(service, likedBookMapper, bookMapper);
    }

    @Test
    void testGetLikedBookById_Success() {
        LikedBookId id = new LikedBookId(1L, 2L);

        User user = new User(1L);
        user.setUsername("TestUser");

        Book book = new Book(2L, "Title", "ISBN", "Author", null, null, null, null, null, null, null);

        LikedBook likedBook = new LikedBook(1L, user, book);

        when(service.getById(id)).thenReturn(Optional.of(likedBook));

        ResponseEntity<LikedBookResponseDTO> response = controller.getLikedBookById(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getUser().getId());
        assertEquals("TestUser", response.getBody().getUser().getUsername());
        assertEquals(2L, response.getBody().getBook().getId());
    }

    @Test
    void testGetLikedBookById_NotFound() {
        LikedBookId id = new LikedBookId(1L, 2L);
        when(service.getById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> controller.getLikedBookById(1L, 2L));
    }

    @Test
    void testCreateLikedBook() {
        LikedBookRequestDTO request = new LikedBookRequestDTO();
        request.setUserId(1L);
        request.setBookId(2L);

        LikedBook likedBook = likedBookMapper.toDomain(request);
        LikedBook saved = new LikedBook(99L, likedBook.getUser(), likedBook.getBook());

        when(service.create(any(LikedBook.class))).thenReturn(saved);

        ResponseEntity<LikedBookResponseDTO> response = controller.createLikedBook(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getUser().getId());
        assertEquals(2L, response.getBody().getBook().getId());
    }

    @Test
    void testDeleteLikedBook_Success() {
        LikedBookId id = new LikedBookId(1L, 2L);

        LikedBook likedBook = new LikedBook(1L, new User(1L), new Book());

        when(service.getById(id)).thenReturn(Optional.of(likedBook));

        ResponseEntity<Void> response = controller.deleteLikedBookById(1L, 2L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).delete(id);
    }

    @Test
    void testDeleteLikedBook_NotFound() {
        LikedBookId id = new LikedBookId(1L, 2L);
        when(service.getById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> controller.deleteLikedBookById(1L, 2L));
    }

    @Test
    void testGetLikedBooksByUserId() {
        Book book = new Book(2L, "Title", "1234567890", "Author", "Description", null, 200, "Publisher", 2020, "Fiction", "EN");
        // ID setzen
        book.setId(2L);

        when(service.getLikedBooksByUserId(1L)).thenReturn(List.of(book));

        ResponseEntity<List<BookResponseDTO>> response = controller.getLikedBooksByUserId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Title", response.getBody().get(0).getTitle());
    }
}
