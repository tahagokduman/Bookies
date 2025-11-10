package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.ILikedCommentService;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ICommentRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LikedCommentServiceIntegrationTest {

    @Autowired
    private ILikedCommentService likedCommentService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private ICommentRepository commentRepository;

    private User testUser;
    private Book testBook;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@example.com", "password", LocalDate.now());
        testUser = userRepository.save(testUser);

        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setIsbn("978-3-16-148410-0"); // Pflichtfeld
        testBook.setAuthor("Test Author"); // Pflichtfeld
        testBook = bookRepository.save(testBook);

        testComment = new Comment(testUser, testBook, 5, "Great book!");
        testComment = commentRepository.save(testComment);
    }

    @Test
    public void testCreateAndGetLikedComment() {
        LikedComment likedComment = new LikedComment();
        likedComment.setUser(testUser);
        likedComment.setComment(testComment);

        LikedComment created = likedCommentService.create(likedComment);
        assertThat(created.getId()).isNotNull();

        LikedCommentId id = new LikedCommentId(testUser.getId(), testComment.getId());
        Optional<LikedComment> found = likedCommentService.getById(id);
        assertThat(found).isPresent();
    }

    @Test
    public void testCountByCommentId() {
        LikedComment likedComment = new LikedComment();
        likedComment.setUser(testUser);
        likedComment.setComment(testComment);
        likedCommentService.create(likedComment);

        Long count = likedCommentService.countByCommentId(testComment.getId());
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void testDeleteLikedComment() {
        LikedComment likedComment = new LikedComment();
        likedComment.setUser(testUser);
        likedComment.setComment(testComment);
        likedCommentService.create(likedComment);

        LikedCommentId id = new LikedCommentId(testUser.getId(), testComment.getId());
        likedCommentService.delete(id);

        Optional<LikedComment> deleted = likedCommentService.getById(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    public void testEntityNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            likedCommentService.countByCommentId(999L);
        });
    }
}