package com.bookapp.UseCaseTests;

import com.bookapp.backend.application.service.LikedBookUseCases;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ILikedBookRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikedBookUseCasesTest {

    private ILikedBookRepository likedBookRepository;
    private IBookRepository bookRepository;
    private IUserRepository userRepository;
    private LikedBookUseCases likedBookUseCases;

    private final LikedBookId likedBookId = new LikedBookId(1L, 1L);
    private final User user = new User(1L);
    private final Book book = new Book(1L);
    private final LikedBook likedBook = new LikedBook(1L, user, book);

    @BeforeEach
    void setUp() {
        likedBookRepository = mock(ILikedBookRepository.class);
        bookRepository = mock(IBookRepository.class);
        userRepository = mock(IUserRepository.class);

        likedBookUseCases = new LikedBookUseCases(likedBookRepository, bookRepository, userRepository);
    }

    @Test
    void testGetAll() {
        when(likedBookRepository.findAll()).thenReturn(List.of(likedBook));
        List<LikedBook> result = likedBookUseCases.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(likedBook, result.get(0));
    }

    @Test
    void testGetByIdFound() {
        when(likedBookRepository.findById(likedBookId)).thenReturn(Optional.of(likedBook));
        Optional<LikedBook> result = likedBookUseCases.getById(likedBookId);
        assertTrue(result.isPresent());
        assertEquals(likedBook, result.get());
    }

    @Test
    void testGetByIdNotFound() {
        when(likedBookRepository.findById(likedBookId)).thenReturn(Optional.empty());
        Optional<LikedBook> result = likedBookUseCases.getById(likedBookId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreate() {
        when(likedBookRepository.save(likedBook)).thenReturn(likedBook);
        LikedBook result = likedBookUseCases.create(likedBook);
        assertNotNull(result);
        assertEquals(likedBook, result);
    }

    @Test
    void testDelete() {
        doNothing().when(likedBookRepository).deleteById(likedBookId);
        likedBookUseCases.delete(likedBookId);
        verify(likedBookRepository, times(1)).deleteById(likedBookId);
    }

    @Test
    void testUpdateThrowsException() {
        Exception exception = assertThrows(UnsupportedOperationException.class,
                () -> likedBookUseCases.update(likedBookId, likedBook));
        assertEquals("LikedBook kann nicht aktualisiert werden.", exception.getMessage());
    }

    @Test
    void testCountByBookId() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(likedBookRepository.countByBookId(book.getId())).thenReturn(5);
        int count = likedBookUseCases.countByBookId(book.getId());
        assertEquals(5, count);
    }

    @Test
    void testGetLikedBooksByUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(likedBookRepository.getLikedBooksByUserId(user.getId())).thenReturn(List.of(book));
        List<Book> books = likedBookUseCases.getLikedBooksByUserId(user.getId());
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(book, books.get(0));
    }

    @Test
    void testEnsureBookExistThrowsException() throws Exception {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        var method = likedBookUseCases.getClass().getDeclaredMethod("ensureBookExist", Long.class);
        method.setAccessible(true);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            try {
                method.invoke(likedBookUseCases, book.getId());
            } catch (Exception e) {
                throw e.getCause();
            }
        });

        assertTrue(exception.getMessage().contains("Book with id " + book.getId() + " was not found"));
    }

    @Test
    void testEnsureUserExistThrowsException() throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        var method = likedBookUseCases.getClass().getDeclaredMethod("ensureUserExist", Long.class);
        method.setAccessible(true);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            try {
                method.invoke(likedBookUseCases, user.getId());
            } catch (Exception e) {
                throw e.getCause();
            }
        });

        assertTrue(exception.getMessage().contains("User with id " + user.getId() + " was not found"));
    }
}
