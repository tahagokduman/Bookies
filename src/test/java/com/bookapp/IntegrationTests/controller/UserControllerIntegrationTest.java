package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.user.UserUpdateRequestDTO;
import com.bookapp.backend.domain.model.user.User;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private User otherUser;
    private String jwtToken;
    private String otherUserJwtToken;
    private final String loginEndpoint = "/api/auth/login";

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@bookapp.com");
        testUser.setPassword(passwordEncoder.encode("Test1234!"));
        testUser.setBirthdayDate(LocalDate.of(1990, 1, 1));
        testUser = userRepository.save(testUser);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@bookapp.com");
        otherUser.setPassword(passwordEncoder.encode("Test1234!"));
        otherUser.setBirthdayDate(LocalDate.of(1995, 5, 5));
        otherUser = userRepository.save(otherUser);

        String loginRequestJson = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", "testuser", "Test1234!");
        String responseContent = mockMvc.perform(post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andReturn().getResponse().getContentAsString();
        jwtToken = objectMapper.readTree(responseContent).get("token").asText();

        String otherLoginRequestJson = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", "otheruser", "Test1234!");
        String otherResponseContent = mockMvc.perform(post(loginEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(otherLoginRequestJson))
                .andReturn().getResponse().getContentAsString();
        otherUserJwtToken = objectMapper.readTree(otherResponseContent).get("token").asText();
    }

    @Test
    @DisplayName("GET /api/users - Soll paginierte Benutzerliste zurückgeben")
    void getUsersPaging_ShouldReturnPaginatedUsers() throws Exception {
        mockMvc.perform(get("/api/users?page=0&size=10")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseDTOList", hasSize(2))) // testUser und otherUser
                .andExpect(jsonPath("$._embedded.userResponseDTOList[0].username", is(testUser.getUsername())))
                .andExpect(jsonPath("$._embedded.userResponseDTOList[1].username", is(otherUser.getUsername())))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.first.href").exists())
                .andExpect(jsonPath("$._links.last.href").exists());
    }

    @Test
    @DisplayName("GET /api/users/{id} - Soll Benutzer nach ID zurückgeben")
    void getUserById_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", testUser.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/users/" + testUser.getId())));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Soll 404 zurückgeben, wenn Benutzer nicht gefunden")
    void getUserById_WhenNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 999L)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/users/{id} - Soll Benutzer erfolgreich aktualisieren")
    void updateUserById_ShouldReturnOk() throws Exception {
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO();
        updateRequestDTO.setUsername("updateduser");
        updateRequestDTO.setEmail("updated@bookapp.com");
        updateRequestDTO.setPassword("NewPass123!");
        updateRequestDTO.setBirthdayDate(LocalDate.of(1991, 2, 2));

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updateduser")))
                .andExpect(jsonPath("$.email", is("updated@bookapp.com")));

        Optional<User> updatedUser = userRepository.findById(testUser.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getUsername()).isEqualTo("updateduser");
    }

    @Test
    @DisplayName("PUT /api/users/{id} - Soll 403 Forbidden zurückgeben, wenn Benutzer nicht der Besitzer ist")
    void updateUserById_WhenNotOwner_ShouldReturnForbidden() throws Exception {
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO();
        updateRequestDTO.setUsername("unauthorizedupdate");
        updateRequestDTO.setEmail("unauthorized@bookapp.com");
        updateRequestDTO.setPassword("NewPass123!");
        updateRequestDTO.setBirthdayDate(LocalDate.of(1991, 2, 2));

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .header("Authorization", "Bearer " + otherUserJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Soll Benutzer erfolgreich löschen")
    void deleteUserById_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUser.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        Optional<User> deletedUser = userRepository.findById(testUser.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Soll 403 Forbidden zurückgeben, wenn Benutzer nicht der Besitzer ist")
    void deleteUserById_WhenNotOwner_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUser.getId())
                        .header("Authorization", "Bearer " + otherUserJwtToken))
                .andExpect(status().isForbidden());
    }



    @Test
    @DisplayName("GET /api/users/search/{keyword} - Soll Benutzer nach Schlüsselwort suchen")
    void searchUser_ShouldReturnMatchingUsers() throws Exception {
        User userToFind = new User();
        userToFind.setUsername("findmeuser");
        userToFind.setEmail("findme@bookapp.com");
        userToFind.setPassword(passwordEncoder.encode("Password123!"));
        userToFind.setBirthdayDate(LocalDate.of(2000, 1, 1));
        userRepository.save(userToFind);

        mockMvc.perform(get("/api/users/search/{keyword}?page=0&size=10", "findme")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.userResponseDTOList[0].username", is("findmeuser")));
    }
}