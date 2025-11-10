package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.avatar.UserAvatarRequestDto;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.model.user.UserAvatar;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserAvatarRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserAvatarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAvatarRepository avatarRepository;

    @Autowired
    private IUserAvatarRepository userAvatarRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private User otherUser;
    private Avatar testAvatar;
    private Avatar newAvatar;
    private String jwtToken;
    private String otherUserJwtToken;
    private final String loginEndpoint = "/api/auth/login";

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@bookapp.com");
        testUser.setPassword(passwordEncoder.encode("Test1234!"));
        testUser = userRepository.save(testUser);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@bookapp.com");
        otherUser.setPassword(passwordEncoder.encode("Test1234!"));
        otherUser = userRepository.save(otherUser);

        // Test-Avatare erstellen
        testAvatar = new Avatar("avatar_url_1");
        testAvatar = avatarRepository.save(testAvatar);

        newAvatar = new Avatar("avatar_url_2");
        newAvatar = avatarRepository.save(newAvatar);

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
    @DisplayName("POST /api/user-avatars - Soll eine User-Avatar-Beziehung erstellen")
    void createUserAvatar_ShouldReturnCreated() throws Exception {
        UserAvatarRequestDto requestDto = new UserAvatarRequestDto(testUser.getId(), testAvatar.getId());

        mockMvc.perform(post("/api/user-avatars")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.avatarId", is(testAvatar.getId().intValue())))
                .andExpect(jsonPath("$.avatar", is(testAvatar.getAvatar())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/user-avatars/" + testUser.getId())));

        Optional<UserAvatar> savedUserAvatar = userAvatarRepository.findById(new UserAvatarId(testUser.getId(), testAvatar.getId()));
        assertThat(savedUserAvatar).isPresent();
    }

    @Test
    @DisplayName("POST /api/user-avatars - Soll 403 Forbidden zurückgeben, wenn userId nicht mit Principal übereinstimmt")
    void createUserAvatar_WhenUserIdDoesNotMatchPrincipal_ShouldReturnForbidden() throws Exception {
        UserAvatarRequestDto requestDto = new UserAvatarRequestDto(otherUser.getId(), testAvatar.getId());

        mockMvc.perform(post("/api/user-avatars")
                        .header("Authorization", "Bearer " + jwtToken) // Token von testUser
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/user-avatars/{userId} - Soll User-Avatar-Beziehung abrufen")
    void getUserAvatarById_ShouldReturnUserAvatar() throws Exception {
        userAvatarRepository.save(new UserAvatar(new UserAvatarId(testUser.getId(), testAvatar.getId()), testUser, testAvatar));

        mockMvc.perform(get("/api/user-avatars/{userId}", testUser.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.avatarId", is(testAvatar.getId().intValue())))
                .andExpect(jsonPath("$.avatar", is(testAvatar.getAvatar())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/user-avatars/" + testUser.getId())));
    }



    @Test
    @DisplayName("PUT /api/user-avatars/{userId} - Soll User-Avatar-Beziehung aktualisieren")
    void updateUserAvatar_ShouldReturnOk() throws Exception {
        userAvatarRepository.save(new UserAvatar(new UserAvatarId(testUser.getId(), testAvatar.getId()), testUser, testAvatar));

        UserAvatarRequestDto requestDto = new UserAvatarRequestDto(testUser.getId(), newAvatar.getId());

        mockMvc.perform(put("/api/user-avatars/{userId}", testUser.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.avatarId", is(newAvatar.getId().intValue())))
                .andExpect(jsonPath("$.avatar", is(newAvatar.getAvatar())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/user-avatars/" + testUser.getId())));

        assertThat(userAvatarRepository.findById(new UserAvatarId(testUser.getId(), testAvatar.getId()))).isEmpty();
        assertThat(userAvatarRepository.findById(new UserAvatarId(testUser.getId(), newAvatar.getId()))).isPresent();
    }

    @Test
    @DisplayName("PUT /api/user-avatars/{userId} - Soll 403 Forbidden zurückgeben, wenn userId nicht mit Principal übereinstimmt")
    void updateUserAvatar_WhenUserIdDoesNotMatchPrincipal_ShouldReturnForbidden() throws Exception {
        userAvatarRepository.save(new UserAvatar(new UserAvatarId(testUser.getId(), testAvatar.getId()), testUser, testAvatar));

        UserAvatarRequestDto requestDto = new UserAvatarRequestDto(testUser.getId(), newAvatar.getId());

        mockMvc.perform(put("/api/user-avatars/{userId}", testUser.getId())
                        .header("Authorization", "Bearer " + otherUserJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }


}