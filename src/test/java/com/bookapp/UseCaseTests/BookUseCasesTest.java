package com.bookapp.UseCaseTests;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.application.service.*;
import com.bookapp.backend.application.config.MostPopularBooksComparator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookUseCasesTest {

    private IBookRepository mockRepository;
    private BookUseCases bookUseCases;
    private MostPopularBooksComparator comparator;

    @BeforeEach
    public void setUp() {
        mockRepository = mock(IBookRepository.class);
        comparator = mock(MostPopularBooksComparator.class);
        bookUseCases = new BookUseCases(mockRepository, comparator);
    }

    @Test
    public void testGetAllBooks() {
        Book book = new Book(1L, "Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.findAll()).thenReturn(List.of(book));

        List<Book> result = bookUseCases.getAll();

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    public void testGetByIdFound() {
        Book book = new Book(1L, "Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookUseCases.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Title", result.get().getTitle());
    }

    @Test
    public void testGetByIdNotFound() {
        when(mockRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookUseCases.getById(1L));
    }

    @Test
    public void testCreateBook() {
        Book book = new Book("Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.save(book)).thenReturn(book);

        Book result = bookUseCases.create(book);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    public void testDeleteBook() {
        when(mockRepository.findById(1L)).thenReturn(Optional.of(new Book(1L)));

        bookUseCases.delete(1L);

        verify(mockRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteBookNotFound() {
        when(mockRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookUseCases.delete(1L));
    }

    @Test
    public void testUpdateBook() {
        Book existing = new Book(1L, "Old", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        Book update = new Book("New", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");

        when(mockRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(mockRepository.update(update, 1L)).thenReturn(update);

        Book result = bookUseCases.update(1L, update);

        assertEquals("New", result.getTitle());
    }

    @Test
    public void testGetBooksPaging() {
        Book book = new Book(1L, "Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(book)));

        Page<Book> result = bookUseCases.getBooksPaging(0, 10);

        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testSearchBooks() {
        Book book = new Book(1L, "Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.searchBooks("Title", List.of("Fiction"), List.of("EN"), 0, 10))
                .thenReturn(new PageImpl<>(List.of(book)));

        Page<Book> result = bookUseCases.searchBook("Title", List.of("Fiction"), List.of("EN"), 0, 10);

        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testGetPopularBooks() {
        Book book1 = new Book(1L, "Book1", "ISBN1", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        Book book2 = new Book(2L, "Book2", "ISBN2", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");

        when(mockRepository.findAll()).thenReturn(List.of(book1, book2));
        when(comparator.compare(any(), any())).thenReturn(1);

        List<Book> result = bookUseCases.getPopularBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetGenresFromAllBooks() {
        when(mockRepository.findAllGenres()).thenReturn(List.of("Fiction", "Sci-Fi"));

        List<String> result = bookUseCases.getGenresFromAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetLanguagesFromAllBooks() {
        when(mockRepository.findAllLanguages()).thenReturn(List.of("EN", "DE"));

        List<String> result = bookUseCases.getLanguagesFromAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetHighlyRatedBooks() {
        Book book = new Book(1L, "Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.getHighlyRatedBooks()).thenReturn(List.of(book));

        List<Book> result = bookUseCases.getHighlyRatedBooks();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetBooksRandomly() {
        Book book = new Book(1L, "Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.getBooksRandomly()).thenReturn(List.of(book));

        List<Book> result = bookUseCases.getBooksRandomly();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetPopularBooksAmongFollowed() {
        Book book = new Book(1L, "Title", "ISBN", "Author", "Desc", "img", 100, "Pub", 2023, "Fiction", "EN");
        when(mockRepository.getPopularBooksAmongFollowed(1L)).thenReturn(List.of(book));

        List<Book> result = bookUseCases.getPopularBooksAmongFollowed(1L);

        assertEquals(1, result.size());
    }
}