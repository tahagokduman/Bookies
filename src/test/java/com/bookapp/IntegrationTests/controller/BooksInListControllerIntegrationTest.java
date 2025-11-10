package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.list.BooksInListRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.list.BooksInListRemoveRequestDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.IListRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BooksInListControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IBooksInListService booksInListService;

    @Autowired
    private IListRepository listRepository;

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long createdListId;
    private Long createdBookId;
    private String authToken;

    @BeforeEach
    void setup() throws Exception {
        booksInListService.getAll().forEach(b ->
                booksInListService.delete(new BooksInListId(b.getList().getId(), b.getBook().getId())));
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@bookapp.com");
        user.setPassword(passwordEncoder.encode("Test1234!"));
        User savedUser = userRepository.save(user);  // Save user first

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

        List list = new List();
        list.setUser(savedUser);
        list.setName("Test List");
        List savedList = listRepository.save(list);
        createdListId = savedList.getId();
    }

    @Test
    @Order(1)
    @DisplayName("POST /api/books-in-list - Should add book to list")
    void addBookToList_shouldReturn201() throws Exception {
        BooksInListRequestDTO requestDTO = new BooksInListRequestDTO();
        requestDTO.setListId(createdListId);
        requestDTO.setBookId(createdBookId);

        mockMvc.perform(post("/api/books-in-list")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("DELETE /api/books-in-list - Should remove book from list")
    void removeBookFromList_shouldReturn204() throws Exception {
        // First add the book to list
        BooksInList booksInList = new BooksInList();
        booksInList.setList(new List(createdListId));
        booksInList.setBook(new Book(createdBookId));
        booksInListService.create(booksInList);

        BooksInListRemoveRequestDTO requestDTO = new BooksInListRemoveRequestDTO();
        requestDTO.setListId(createdListId);
        requestDTO.setBookId(createdBookId);

        mockMvc.perform(delete("/api/books-in-list")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(3)
    @DisplayName("GET /api/books-in-list/{bookId} - Should get lists containing book")
    void getListsByBookId_shouldReturnLists() throws Exception {
        BooksInList booksInList = new BooksInList();
        booksInList.setList(new List(createdListId));
        booksInList.setBook(new Book(createdBookId));
        booksInListService.create(booksInList);

        mockMvc.perform(get("/api/books-in-list/" + createdBookId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.listResponseDTOList[0].id", is(createdListId.intValue())))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
    @Test
    @Order(4)
    @DisplayName("POST /api/books-in-list - Should return 404 when list is not found")
    void addBookToList_invalidRequest_shouldReturn404() throws Exception {
        String requestWithNulls = """
    {
        "listId": null,
        "bookId": null
    }
    """;

        mockMvc.perform(post("/api/books-in-list")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestWithNulls))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("List with id 0 was not found"));
    }
    @Test
    @Order(5)
    @DisplayName("DELETE /api/books-in-list - Should return 404 for non-existent entry")
    void removeBookFromList_notFound_shouldReturn404() throws Exception {
        BooksInListRemoveRequestDTO requestDTO = new BooksInListRemoveRequestDTO();
        requestDTO.setListId(999L);
        requestDTO.setBookId(999L);

        mockMvc.perform(delete("/api/books-in-list")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    void tearDown() {
        try {
            booksInListService.getAll().forEach(b ->
                    booksInListService.delete(new BooksInListId(b.getList().getId(), b.getBook().getId())));
            userRepository.deleteAll();
        } catch (Exception e) {
        }
    }
}