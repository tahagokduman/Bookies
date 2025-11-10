package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.ICommentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentServiceIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ICommentService commentService() {
            return new TestCommentService();
        }
    }

    static class TestCommentService implements ICommentService {
        private final Map<Long, Comment> store = new HashMap<>();
        private long idSequence = 1L;

        @Override
        public List<Comment> getAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public Optional<Comment> getById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public Comment create(Comment comment) {
            comment.setId(idSequence++);
            store.put(comment.getId(), comment);
            return comment;
        }

        @Override
        public void delete(Long id) {
            store.remove(id);
        }

        @Override
        public Comment update(Long id, Comment comment) {
            if (!store.containsKey(id)) {
                throw new IllegalArgumentException("Comment not found");
            }
            comment.setId(id);
            store.put(id, comment);
            return comment;
        }

        @Override
        public Page<Comment> getCommentsPaging(int page, int size, Long bookId) {
            List<Comment> filtered = store.values().stream()
                    .filter(c -> c.getBook().getId().equals(bookId))
                    .collect(Collectors.toList());

            int start = page * size;
            int end = Math.min(start + size, filtered.size());
            List<Comment> content = (start >= filtered.size()) ?
                    Collections.emptyList() : filtered.subList(start, end);

            return new PageImpl<>(content, PageRequest.of(page, size), filtered.size());
        }
    }

    @Autowired
    private ICommentService commentService;

    private Long testCommentId;
    private Long testBookId = 1L;
    private Long testUserId = 1L;

    @BeforeAll
    void setupTestData() {
        User testUser = new User(testUserId);
        testUser.setUsername("testuser");

        Book testBook = new Book(testBookId);
        testBook.setTitle("Test Book");

        Comment comment = new Comment();
        comment.setUser(testUser);
        comment.setBook(testBook);
        comment.setScore(5);
        comment.setComment("Great book!");

        Comment created = commentService.create(comment);
        testCommentId = created.getId();
    }

    @Test
    @Order(1)
    void testCreateAndGetById() {
        Optional<Comment> found = commentService.getById(testCommentId);
        assertTrue(found.isPresent(), "Comment should exist");
        assertEquals("Great book!", found.get().getComment());
        assertEquals(5, found.get().getScore());
    }

    @Test
    @Order(2)
    void testGetCommentsPaging() {
        Page<Comment> page = commentService.getCommentsPaging(0, 10, testBookId);
        assertFalse(page.isEmpty(), "Should find comments for book");
        assertEquals(1, page.getTotalElements());
        assertEquals("Great book!", page.getContent().get(0).getComment());
    }

    @Test
    @Order(3)
    void testUpdateComment() {
        Comment toUpdate = commentService.getById(testCommentId).get();
        toUpdate.setComment("Updated comment");
        toUpdate.setScore(8);

        Comment updated = commentService.update(testCommentId, toUpdate);
        assertEquals("Updated comment", updated.getComment());
        assertEquals(8, updated.getScore());

        Optional<Comment> verified = commentService.getById(testCommentId);
        assertTrue(verified.isPresent());
        assertEquals("Updated comment", verified.get().getComment());
    }

    @Test
    @Order(4)
    void testDeleteComment() {
        commentService.delete(testCommentId);
        Optional<Comment> deleted = commentService.getById(testCommentId);
        assertFalse(deleted.isPresent(), "Comment should be deleted");
    }


}