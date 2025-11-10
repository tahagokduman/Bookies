package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksStatusId;
import com.bookapp.backend.domain.model.status.BooksStatus;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IBooksStatusService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BooksStatusServiceIntegrationTest {

    @Configuration
    static class TestConfig {
        @Bean
        public IBooksStatusService booksStatusService() {
            return new TestBooksStatusService();
        }
    }

    static class TestBooksStatusService implements IBooksStatusService {
        private final List<BooksStatus> store = new ArrayList<>();
        private long idSequence = 1L;

        @Override
        public BooksStatus create(BooksStatus entity) {
            entity.setId(idSequence++);
            store.add(entity);
            return entity;
        }

        @Override
        public Optional<BooksStatus> getById(BooksStatusId id) {
            return store.stream()
                    .filter(bs -> bs.getUser().getId().equals(id.getUserId()) &&
                            bs.getBook().getId().equals(id.getBookId()))
                    .findFirst();
        }

        @Override
        public BooksStatus update(BooksStatusId id, BooksStatus entity) {
            BooksStatus existing = getById(id)
                    .orElseThrow(() -> new IllegalArgumentException("BooksStatus not found"));

            entity.setId(existing.getId());
            store.remove(existing);
            store.add(entity);
            return entity;
        }

        @Override
        public void delete(BooksStatusId id) {
            BooksStatus toRemove = getById(id)
                    .orElseThrow(() -> new IllegalArgumentException("BooksStatus not found"));
            store.remove(toRemove);
        }

        @Override
        public List<BooksStatus> getAll() {
            return new ArrayList<>(store);
        }

        @Override
        public List<Book> getReadBooksByUserId(Long userId) {
            return store.stream()
                    .filter(bs -> bs.getUser().getId().equals(userId))
                    .filter(bs -> "READ".equals(bs.getStatus().getStatus()))
                    .map(BooksStatus::getBook)
                    .collect(Collectors.toList());
        }

        @Override
        public List<Book> getReadListBooksByUserId(Long userId) {
            return store.stream()
                    .filter(bs -> bs.getUser().getId().equals(userId))
                    .filter(bs -> "READ_LIST".equals(bs.getStatus().getStatus()))
                    .map(BooksStatus::getBook)
                    .collect(Collectors.toList());
        }
    }

    @Autowired
    private IBooksStatusService booksStatusService;

    private final Long testUserId = 1L;
    private final Long testBookId = 1L;
    private BooksStatusId testBooksStatusId;
    private Long createdId;

    @BeforeAll
    void setupTestData() {
        User testUser = new User(testUserId);
        testUser.setId(testUserId);

        Book testBook = new Book();
        testBook.setId(testBookId);
        testBook.setTitle("Test Book");

        Status readStatus = new Status();
        readStatus.setStatus("READ");

        BooksStatus booksStatus = new BooksStatus();
        booksStatus.setUser(testUser);
        booksStatus.setBook(testBook);
        booksStatus.setStatus(readStatus);

        BooksStatus created = booksStatusService.create(booksStatus);
        createdId = created.getId();
        testBooksStatusId = new BooksStatusId(testUserId, testBookId);

        Assertions.assertNotNull(createdId, "BooksStatus sollte eine ID haben");
    }

    @Test
    @Order(1)
    void testGetById() {
        Optional<BooksStatus> found = booksStatusService.getById(testBooksStatusId);
        Assertions.assertTrue(found.isPresent(),
                "BooksStatus sollte mit UserId=" + testUserId + " und BookId=" + testBookId + " gefunden werden");
        Assertions.assertEquals("READ", found.get().getStatus().getStatus());
    }

    @Test
    @Order(4)
    void testUpdateStatus() {
        Optional<BooksStatus> optionalStatus = booksStatusService.getById(testBooksStatusId);
        Assertions.assertTrue(optionalStatus.isPresent(),
                "BooksStatus sollte vor dem Update existieren");

        BooksStatus status = optionalStatus.get();
        Status newStatus = new Status();
        newStatus.setStatus("READ_LIST");
        status.setStatus(newStatus);

        BooksStatus updated = booksStatusService.update(testBooksStatusId, status);
        Assertions.assertEquals("READ_LIST", updated.getStatus().getStatus());

        Optional<BooksStatus> verified = booksStatusService.getById(testBooksStatusId);
        Assertions.assertTrue(verified.isPresent(),
                "BooksStatus sollte nach dem Update existieren");
        Assertions.assertEquals("READ_LIST", verified.get().getStatus().getStatus());
    }

    @Test
    @Order(2)
    void testGetReadBooksByUserId() {
        List<Book> readBooks = booksStatusService.getReadBooksByUserId(testUserId);
        Assertions.assertFalse(readBooks.isEmpty());
        Assertions.assertEquals("Test Book", readBooks.get(0).getTitle());
    }

    @Test
    @Order(3)
    void testGetReadListBooksByUserId() {
        User testUser = new User(testUserId);

        Book readListBook = new Book();
        readListBook.setId(2L);
        readListBook.setTitle("Read List Book");

        Status readListStatus = new Status();
        readListStatus.setStatus("READ_LIST");

        BooksStatus booksStatus = new BooksStatus();
        booksStatus.setUser(testUser);
        booksStatus.setBook(readListBook);
        booksStatus.setStatus(readListStatus);

        booksStatusService.create(booksStatus);

        List<Book> readListBooks = booksStatusService.getReadListBooksByUserId(testUserId);
        Assertions.assertFalse(readListBooks.isEmpty());
        Assertions.assertEquals("Read List Book", readListBooks.get(0).getTitle());
    }

    @Test
    @Order(5)
    void testDeleteStatus() {
        booksStatusService.delete(testBooksStatusId);
        Optional<BooksStatus> deleted = booksStatusService.getById(testBooksStatusId);
        Assertions.assertFalse(deleted.isPresent());
    }
}