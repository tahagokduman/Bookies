package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.book.BookCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.book.BookUpdateRequestDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IBookRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long createdBookId;
    private String authToken;

    @BeforeEach
    void setup() throws Exception {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@bookapp.com");
        user.setPassword(passwordEncoder.encode("Test1234!"));
        userRepository.save(user);

        String loginRequest = """
        {
            "username": "testuser",
            "password": "Test1234!"
        }
        """;

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();

        authToken = objectMapper.readTree(response).get("token").asText();

        Book book = new Book(
                "Test Book",
                "1234567890123",
                "Test Author",
                "Test Description",
                "http://test.com/cover.jpg",
                200,
                "Test Publisher",
                2023,
                "Fiction",
                "English"
        );
        Book savedBook = bookRepository.save(book);
        createdBookId = savedBook.getId();
    }

    @Test
    @Order(1)
    @DisplayName("GET /api/books - Should return paginated books")
    void getAllBooks_shouldReturnPaginatedBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .header("Authorization", "Bearer " + authToken)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bookResponseDTOList[0].title",
                        is("Test Book")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @Order(2)
    @DisplayName("GET /api/books/{id} - Should return book by ID")
    void getBookById_shouldReturnBook() throws Exception {
        mockMvc.perform(get("/api/books/" + createdBookId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Book")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/books - Should create new book")
    void createBook_shouldReturnCreatedBook() throws Exception {
        BookCreateRequestDTO requestDTO = new BookCreateRequestDTO();
        requestDTO.setTitle("New Book");
        requestDTO.setIsbn("1234567890124");
        requestDTO.setAuthor("New Author");
        requestDTO.setDescription("New Description");
        requestDTO.setCoverImageUrl("http://test.com/new.jpg");
        requestDTO.setPageCount(300);
        requestDTO.setPublisher("New Publisher");
        requestDTO.setPublishedYear(2024);
        requestDTO.setGenre("Science Fiction");
        requestDTO.setLanguage("German");

        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @Order(4)
    @DisplayName("PUT /api/books/{id} - Should update book")
    void updateBook_shouldReturnUpdatedBook() throws Exception {
        BookUpdateRequestDTO requestDTO = new BookUpdateRequestDTO();
        requestDTO.setTitle("Updated Book");
        requestDTO.setIsbn("1234567890123");
        requestDTO.setDescription("Updated Description");
        requestDTO.setCoverImageUrl("http://test.com/updated.jpg");

        mockMvc.perform(put("/api/books/" + createdBookId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Book")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @Order(5)
    @DisplayName("DELETE /api/books/{id} - Should delete book")
    void deleteBook_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/books/" + createdBookId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/books/" + createdBookId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    @DisplayName("GET /api/books/search - Should search books")
    void searchBook_shouldReturnFilteredBooks() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .header("Authorization", "Bearer " + authToken)
                        .param("keyword", "Test")
                        .param("genres", "Fiction")
                        .param("languages", "English")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bookResponseDTOList[0].title",
                        is("Test Book")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @AfterEach
    void tearDown() {
        try {
            userRepository.deleteAll();
        } catch (Exception e) {
        }
    }
}