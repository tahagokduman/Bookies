package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.comment.LikedCommentCreateRequestDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ICommentRepository;
import com.bookapp.backend.domain.ports.out.ILikedCommentRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LikedCommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private ILikedCommentRepository likedCommentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Book testBook;
    private Comment testComment;
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setEmail("test@test.com");
        testUser = userRepository.save(testUser);

        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setIsbn("978-3-16-148410-0");
        testBook.setAuthor("Test Author");
        testBook.setDescription("Test Description");
        testBook.setCoverImageUrl("test_cover.jpg");
        testBook.setPageCount(100);
        testBook.setPublisher("Test Publisher");
        testBook.setPublishedYear(2023);
        testBook.setGenre("Fiction");
        testBook.setLanguage("German");
        testBook = bookRepository.save(testBook);

        testComment = new Comment();
        testComment.setUser(testUser);
        testComment.setBook(testBook);
        testComment.setComment("Test comment");
        testComment.setScore(5);
        testComment = commentRepository.save(testComment);

        String loginRequestJson = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", "testuser", "password123");

        String responseContent = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(responseContent).get("token").asText();
    }

    @Test
    @DisplayName("POST /api/liked-comments - Soll einen Kommentar erfolgreich liken")
    void likeComment_ShouldReturn201() throws Exception {
        LikedCommentCreateRequestDTO request = new LikedCommentCreateRequestDTO();
        request.setUserId(testUser.getId());
        request.setCommentId(testComment.getId());

        mockMvc.perform(post("/api/liked-comments")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertTrue(likedCommentRepository.existsByUserIdAndCommentId(
                testUser.getId(),
                testComment.getId()
        ));
    }

    @Test
    @DisplayName("GET /is-liked - Soll den Like-Status korrekt zurückgeben")
    void isLiked_ShouldReturnTrueAfterLike() throws Exception {
        LikedCommentCreateRequestDTO request = new LikedCommentCreateRequestDTO();
        request.setUserId(testUser.getId());
        request.setCommentId(testComment.getId());

        mockMvc.perform(post("/api/liked-comments")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/liked-comments/user/{userId}/comment/{commentId}/is-liked",
                        testUser.getId(), testComment.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("GET /comment/{commentId}/count - Soll die korrekte Anzahl der Likes zurückgeben")
    void countLikes_ShouldReturnCorrectCount() throws Exception {
        LikedCommentCreateRequestDTO request = new LikedCommentCreateRequestDTO();
        request.setUserId(testUser.getId());
        request.setCommentId(testComment.getId());

        mockMvc.perform(post("/api/liked-comments")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/liked-comments/comment/{commentId}/count",
                        testComment.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("DELETE /unlike - Soll 204 zurückgeben")
    void unlike_ShouldReturn204() throws Exception {
        LikedCommentCreateRequestDTO request = new LikedCommentCreateRequestDTO();
        request.setUserId(testUser.getId());
        request.setCommentId(testComment.getId());

        mockMvc.perform(post("/api/liked-comments")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(delete("/api/liked-comments/user/{userId}/comment/{commentId}",
                        testUser.getId(), testComment.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());
    }


}