package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.in.IBookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class BookServiceIntegrationTest {

    @Autowired
    private IBookService bookService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("DELETE FROM books_in_list");
        jdbcTemplate.execute("DELETE FROM books");
    }

    @Test
    void createBook_and_fetchById() {
        Book book = Book.builder()
                .title("Test Book")
                .isbn("1234567890123")
                .author("Test Author")
                .description("Test description")
                .coverImageUrl("http://example.com/cover.jpg")
                .pageCount(200)
                .publisher("Test Publisher")
                .publishedYear(2023)
                .genre("Fiction")
                .language("English")
                .build();

        Book created = bookService.create(book);
        assertNotNull(created.getId());

        Optional<Book> fetched = bookService.getById(created.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Test Book", fetched.get().getTitle());
    }

    @Test
    void updateBook_shouldChangeFields() {
        Book book = Book.builder()
                .title("Old Title")
                .isbn("1111111111111")
                .author("Author")
                .description("Desc")
                .coverImageUrl("url")
                .pageCount(100)
                .publisher("Pub")
                .publishedYear(2020)
                .genre("Drama")
                .language("Turkish")
                .build();

        Book created = bookService.create(book);
        created.setTitle("New Title");

        Book updated = bookService.update(created.getId(), created);
        assertEquals("New Title", updated.getTitle());
    }

    @Test
    void deleteBook_shouldRemoveFromDB() {
        Book book = Book.builder()
                .title("Delete Me")
                .isbn("9999999999999")
                .author("X")
                .description("Y")
                .coverImageUrl("url")
                .pageCount(100)
                .publisher("Z")
                .publishedYear(2015)
                .genre("Horror")
                .language("German")
                .build();

        Book created = bookService.create(book);
        Long id = created.getId();

        bookService.delete(id);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> bookService.getById(id));
        assertTrue(thrown.getMessage().contains("was not found"));
    }

    @Test
    void getAllBooks_shouldReturnList() {
        Book book1 = Book.builder()
                .title("Book 1")
                .isbn("0000000000001")
                .author("Author 1")
                .description("Desc 1")
                .coverImageUrl("url1")
                .pageCount(101)
                .publisher("Pub 1")
                .publishedYear(2021)
                .genre("Genre 1")
                .language("Lang 1")
                .build();

        Book book2 = Book.builder()
                .title("Book 2")
                .isbn("0000000000002")
                .author("Author 2")
                .description("Desc 2")
                .coverImageUrl("url2")
                .pageCount(102)
                .publisher("Pub 2")
                .publishedYear(2022)
                .genre("Genre 2")
                .language("Lang 2")
                .build();

        bookService.create(book1);
        bookService.create(book2);

        List<Book> books = bookService.getAll();
        assertTrue(books.size() >= 2);
    }
}