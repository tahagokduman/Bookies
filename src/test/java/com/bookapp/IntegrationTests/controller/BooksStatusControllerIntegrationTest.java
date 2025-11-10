package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.status.BooksStatusRequestDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.IBooksStatusRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BooksStatusControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IBooksStatusRepository booksStatusRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long testUserId;
    private Long testBookId1;
    private Long testBookId2;
    private String authToken;
    private final String loginEndpoint = "/api/auth/login";

    @BeforeEach
    void setup() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@bookapp.com");
        user.setPassword(passwordEncoder.encode("Test1234!"));
        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();

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

        Book book1 = new Book("Test Book 1", "978-0000000001", "Author A", "Desc A", "cover_a.jpg", 100, "Pub A", 2020, "Genre A", "English");
        bookRepository.save(book1);
        testBookId1 = book1.getId();

        Book book2 = new Book("Test Book 2", "978-0000000002", "Author B", "Desc B", "cover_b.jpg", 200, "Pub B", 2021, "Genre B", "German");
        bookRepository.save(book2);
        testBookId2 = book2.getId();
    }




    @Test
    @DisplayName("POST /api/books-status/read-list - Soll 403 Forbidden zurückgeben, wenn userId nicht mit Principal übereinstimmt")
    void addBookToReadList_UnauthorizedUser_Returns403Forbidden() throws Exception {
        BooksStatusRequestDTO requestDTO = new BooksStatusRequestDTO();
        requestDTO.setUserId(1000L);
        requestDTO.setBookId(testBookId1);

        mockMvc.perform(post("/api/books-status/read-list")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/books-status/read-list/{userId} - Soll 404 Not Found zurückgeben, wenn der Benutzer nicht existiert")
    void getReadListByUserId_NonExistentUser_Returns404NotFound() throws Exception {
        mockMvc.perform(get("/api/books-status/read-list/{userId}", 1000L)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}