package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.BookController;
import com.bookapp.backend.adapter.in.web.dto.request.book.BookCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.book.BookUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.in.IBookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private IBookService bookService;

    @Mock
    private BookWebMapper bookWebMapper;

    @InjectMocks
    private BookController bookController;

    private Book exampleBook;
    private BookResponseDTO exampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        exampleBook = new Book();
        exampleBook.setId(1L);
        exampleBook.setTitle("Example Book");

        exampleDto = new BookResponseDTO();
        exampleDto.setId(1L);
        exampleDto.setTitle("Example Book");
    }

    @Test
    void testGetAllBooks_ReturnsPagedBooksWithLinks() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<Book> bookPage = new PageImpl<>(List.of(exampleBook));

        when(bookService.getBooksPaging(0, 12)).thenReturn(bookPage);
        when(bookWebMapper.toResponseDto(exampleBook)).thenReturn(exampleDto);

        ResponseEntity<CollectionModel<BookResponseDTO>> response = bookController.getAllBooks(0, 12);

        assertEquals(200, response.getStatusCodeValue());
        CollectionModel<BookResponseDTO> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getContent().size());
        assertTrue(body.getContent().contains(exampleDto));

        verify(bookService, times(1)).getBooksPaging(0, 12);
        verify(bookWebMapper, times(1)).toResponseDto(exampleBook);
    }

    @Test
    void testGetBookById_Found_ReturnsBookResponse() {
        when(bookService.getById(1L)).thenReturn(Optional.of(exampleBook));
        when(bookWebMapper.toResponseDto(exampleBook)).thenReturn(exampleDto);

        ResponseEntity<BookResponseDTO> response = bookController.getBookById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(exampleDto, response.getBody());

        verify(bookService).getById(1L);
        verify(bookWebMapper).toResponseDto(exampleBook);
    }

    @Test
    void testGetBookById_NotFound_ThrowsException() {
        when(bookService.getById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            bookController.getBookById(1L);
        });

        assertTrue(thrown.getMessage().contains("Book with id 1 not found"));
        verify(bookService).getById(1L);
        verifyNoInteractions(bookWebMapper);
    }

    @Test
    void testCreateBook_ReturnsCreatedBook() {
        BookCreateRequestDTO createDto = new BookCreateRequestDTO();

        when(bookWebMapper.toDomain(createDto)).thenReturn(exampleBook);
        when(bookService.create(exampleBook)).thenReturn(exampleBook);
        when(bookWebMapper.toResponseDto(exampleBook)).thenReturn(exampleDto);

        ResponseEntity<BookResponseDTO> response = bookController.createBook(createDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(exampleDto, response.getBody());

        verify(bookWebMapper).toDomain(createDto);
        verify(bookService).create(exampleBook);
        verify(bookWebMapper).toResponseDto(exampleBook);
    }

    @Test
    void testUpdateBook_BookExists_ReturnsUpdatedBook() {
        BookUpdateRequestDTO updateDto = new BookUpdateRequestDTO();

        when(bookWebMapper.toDomain(updateDto)).thenReturn(exampleBook);
        when(bookService.update(1L, exampleBook)).thenReturn(exampleBook);
        when(bookWebMapper.toResponseDto(exampleBook)).thenReturn(exampleDto);

        ResponseEntity<BookResponseDTO> response = bookController.updateBook(1L, updateDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(exampleDto, response.getBody());

        verify(bookWebMapper).toDomain(updateDto);
        verify(bookService).update(1L, exampleBook);
        verify(bookWebMapper).toResponseDto(exampleBook);
    }

    @Test
    void testDeleteBook_BookExists_DeletesAndReturnsNoContent() {
        when(bookService.getById(1L)).thenReturn(Optional.of(exampleBook));
        doNothing().when(bookService).delete(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(204, response.getStatusCodeValue());

        verify(bookService).getById(1L);
        verify(bookService).delete(1L);
    }

    @Test
    void testDeleteBook_BookNotFound_ThrowsException() {
        when(bookService.getById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            bookController.deleteBook(1L);
        });

        assertTrue(thrown.getMessage().contains("Book with id 1 not found"));

        verify(bookService).getById(1L);
        verify(bookService, never()).delete(anyLong());
    }

    @Test
    void testSearchBook_ReturnsPagedBooks() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<Book> bookPage = new PageImpl<>(List.of(exampleBook));
        List<String> genres = Collections.emptyList();
        List<String> languages = Collections.emptyList();

        when(bookService.searchBook("example", genres, languages, 0, 12)).thenReturn(bookPage);
        when(bookWebMapper.toResponseDto(exampleBook)).thenReturn(exampleDto);

        ResponseEntity<CollectionModel<BookResponseDTO>> response = bookController.searchBook("example", genres, languages, 0, 12);

        assertEquals(200, response.getStatusCodeValue());
        CollectionModel<BookResponseDTO> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getContent().size());
        assertTrue(body.getContent().contains(exampleDto));

        verify(bookService).searchBook("example", genres, languages, 0, 12);
        verify(bookWebMapper).toResponseDto(exampleBook);
    }


}
