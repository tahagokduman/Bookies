package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.follow.LikedBookRequestDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ILikedBookRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LikedBookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private ILikedBookRepository likedBookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long testUserId;
    private Long otherUserId;
    private Long testBookId;
    private Long otherBookId;
    private String authToken;
    private String otherUserAuthToken;
    private final String loginEndpoint = "/api/auth/login";

    @BeforeEach
    void setup() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@bookapp.com");
        user.setPassword(passwordEncoder.encode("Test1234!"));
        testUserId = userRepository.save(user).getId();

        User otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@bookapp.com");
        otherUser.setPassword(passwordEncoder.encode("Test1234!"));
        otherUserId = userRepository.save(otherUser).getId();

        Book book = new Book("Test Book", "978-0000000001", "Author A", "Desc A", "cover_a.jpg", 100, "Pub A", 2020, "Genre A", "English");
        testBookId = bookRepository.save(book).getId();

        Book otherBook = new Book("Other Book", "978-0000000002", "Author B", "Desc B", "cover_b.jpg", 200, "Pub B", 2021, "Genre B", "English");
        otherBookId = bookRepository.save(otherBook).getId();


        String loginRequest = String.format("""
            {
                "username": "%s",
                "password": "%s"
            }
            """, "testuser", "Test1234!");

        String response = mockMvc.perform(post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        authToken = objectMapper.readTree(response).get("token").asText();

        String otherLoginRequest = String.format("""
            {
                "username": "%s",
                "password": "%s"
            }
            """, "otheruser", "Test1234!");

        String otherResponse = mockMvc.perform(post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otherLoginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        otherUserAuthToken = objectMapper.readTree(otherResponse).get("token").asText();
    }

    @Test
    @Order(1)
    @DisplayName("POST /api/liked-books - Soll ein neues geliktes Buch erstellen")
    void createLikedBook_shouldSucceed() throws Exception {
        LikedBookRequestDTO requestDTO = new LikedBookRequestDTO();
        requestDTO.setUserId(testUserId);
        requestDTO.setBookId(testBookId);

        mockMvc.perform(post("/api/liked-books")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id", is(testUserId.intValue())))
                .andExpect(jsonPath("$.book.id", is(testBookId.intValue())))
                .andExpect(jsonPath("$._links.self.href", notNullValue()));

        Optional<LikedBook> savedLikedBook = likedBookRepository.findById(new LikedBookId(testUserId, testBookId));
        assertThat(savedLikedBook).isPresent();
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/liked-books - Soll 403 Forbidden zurückgeben, wenn ein anderer Benutzer ein Buch liken möchte")
    void createLikedBook_UnauthorizedUser_Returns403Forbidden() throws Exception {
        LikedBookRequestDTO requestDTO = new LikedBookRequestDTO();
        requestDTO.setUserId(testUserId); // Liked Book auf User A setzen
        requestDTO.setBookId(testBookId);

        mockMvc.perform(post("/api/liked-books")
                        .header("Authorization", "Bearer " + otherUserAuthToken) // Mit Token von User B senden
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("GET /api/liked-books/user/{userId}/book/{bookId} - Soll ein geliktes Buch nach ID zurückgeben")
    void getLikedBookById_shouldSucceed() throws Exception {
        likedBookRepository.save(new LikedBook(testUserId, new User(testUserId), new Book(testBookId)));

        mockMvc.perform(get("/api/liked-books/user/{userId}/book/{bookId}", testUserId, testBookId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(testUserId.intValue())))
                .andExpect(jsonPath("$.book.id", is(testBookId.intValue())))
                .andExpect(jsonPath("$._links.self.href", notNullValue()));
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/liked-books/user/{userId}/book/{bookId} - Soll 404 zurückgeben, wenn geliktes Buch nicht existiert")
    void getLikedBookById_NotFound() throws Exception {
        mockMvc.perform(get("/api/liked-books/user/{userId}/book/{bookId}", testUserId, 999L)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    @DisplayName("DELETE /api/liked-books/user/{userId}/book/{bookId} - Soll ein geliktes Buch löschen")
    void deleteLikedBook_shouldSucceed() throws Exception {
        likedBookRepository.save(new LikedBook(testUserId, new User(testUserId), new Book(testBookId)));

        mockMvc.perform(delete("/api/liked-books/user/{userId}/book/{bookId}", testUserId, testBookId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());

        Optional<LikedBook> deletedLikedBook = likedBookRepository.findById(new LikedBookId(testUserId, testBookId));
        assertThat(deletedLikedBook).isEmpty();
    }

    @Test
    @Order(6)
    @DisplayName("DELETE /api/liked-books/user/{userId}/book/{bookId} - Soll 403 Forbidden zurückgeben, wenn ein anderer Benutzer versucht zu löschen")
    void deleteLikedBook_UnauthorizedUser_Returns403Forbidden() throws Exception {
        likedBookRepository.save(new LikedBook(testUserId, new User(testUserId), new Book(testBookId)));

        mockMvc.perform(delete("/api/liked-books/user/{userId}/book/{bookId}", testUserId, testBookId)
                        .header("Authorization", "Bearer " + otherUserAuthToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(7)
    @DisplayName("GET /api/liked-books/user/{userId} - Soll alle gelikten Bücher eines Benutzers zurückgeben")
    void getLikedBooksByUserId_shouldSucceed() throws Exception {
        likedBookRepository.save(new LikedBook(testUserId, new User(testUserId), new Book(testBookId)));
        likedBookRepository.save(new LikedBook(testUserId, new User(testUserId), new Book(otherBookId)));

        mockMvc.perform(get("/api/liked-books/user/{userId}", testUserId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testBookId.intValue())))
                .andExpect(jsonPath("$[1].id", is(otherBookId.intValue())));
    }
}