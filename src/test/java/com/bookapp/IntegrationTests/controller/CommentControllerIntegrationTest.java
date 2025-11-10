package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentUpdateRequestDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ICommentRepository;
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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBookRepository bookRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long testUserId;
    private Long otherUserId;
    private Long testBookId;
    private String authToken;
    private String otherUserAuthToken;
    private final String loginEndpoint = "/api/auth/login";

    @BeforeEach
    void setup() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@bookapp.com");
        user.setPassword(passwordEncoder.encode("Test1234!"));
        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();

        User otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@bookapp.com");
        otherUser.setPassword(passwordEncoder.encode("Test1234!"));
        User savedOtherUser = userRepository.save(otherUser);
        otherUserId = savedOtherUser.getId();

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

        Book book = new Book("Test Book", "978-0000000001", "Author A", "Desc A", "cover_a.jpg", 100, "Pub A", 2020, "Genre A", "English");
        bookRepository.save(book);
        testBookId = book.getId();
    }



    @Test
    @DisplayName("GET /api/comments/{id} - Soll 404 zurückgeben, wenn Kommentar nicht existiert")
    void getCommentById_NotFound() throws Exception {
        mockMvc.perform(get("/api/comments/{id}", 999L)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }










}