package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.list.ListCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.list.ListUpdateRequestDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.IListRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ListControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IListRepository listRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private User otherUser;
    private Book testBook;
    private List testList;
    private String jwtToken;
    private String otherUserJwtToken;
    private final String loginEndpoint = "/api/auth/login";

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser = userRepository.save(testUser);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@test.com");
        otherUser.setPassword(passwordEncoder.encode("password123"));
        otherUser = userRepository.save(otherUser);

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

        testList = new List();
        testList.setUser(testUser);
        testList.setName("Test List");
        testList = listRepository.save(testList);

        String loginRequestJson = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", "testuser", "password123");
        String responseContent = mockMvc.perform(post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        jwtToken = objectMapper.readTree(responseContent).get("token").asText();

        String otherLoginRequestJson = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", "otheruser", "password123");
        String otherResponseContent = mockMvc.perform(post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otherLoginRequestJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        otherUserJwtToken = objectMapper.readTree(otherResponseContent).get("token").asText();
    }

    @Test
    @DisplayName("POST /api/lists - Soll eine Liste erfolgreich erstellen")
    void createList_ShouldReturn201() throws Exception {
        ListCreateRequestDTO request = new ListCreateRequestDTO();
        request.setUserId(testUser.getId());
        request.setName("New Test List");

        mockMvc.perform(post("/api/lists")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Test List"));
    }

    @Test
    @DisplayName("GET /api/lists/{id} - Soll 200 zurückgeben, wenn eine Liste per ID abgefragt wird")
    void getListById_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/lists/{id}", testList.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testList.getId()))
                .andExpect(jsonPath("$.title").value("Test List"));
    }



    @Test
    @DisplayName("DELETE /api/lists/{id} - Soll 204 zurückgeben, wenn eine Liste gelöscht wird")
    void deleteList_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/lists/{id}", testList.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        assertFalse(listRepository.findById(testList.getId()).isPresent());
    }

    @Test
    @DisplayName("GET /api/lists/user/{id} - Soll 200 zurückgeben und die Listen des Benutzers enthalten")
    void getAllListsFromUser_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/lists/user/{id}", testUser.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test List"));
    }




    @Test
    @DisplayName("POST /api/lists - Soll 403 Forbidden zurückgeben, wenn der Benutzer des Tokens nicht der Ersteller ist")
    void createList_WhenTokenUserIsNotOwner_ShouldReturn403() throws Exception {
        ListCreateRequestDTO request = new ListCreateRequestDTO();
        request.setUserId(otherUser.getId());
        request.setName("New Test List");

        mockMvc.perform(post("/api/lists")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}