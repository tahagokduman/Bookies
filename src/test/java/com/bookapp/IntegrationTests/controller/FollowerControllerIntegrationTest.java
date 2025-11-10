package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.follow.FollowerRequestDTO;
import com.bookapp.backend.domain.model.id.FollowerId;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IFollowerRepository;
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
public class FollowerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IFollowerRepository followerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long testUserId;
    private Long otherUserId;
    private Long thirdUserId;
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

        User thirdUser = new User();
        thirdUser.setUsername("thirduser");
        thirdUser.setEmail("third@bookapp.com");
        thirdUser.setPassword(passwordEncoder.encode("Test1234!"));
        User savedThirdUser = userRepository.save(thirdUser);
        thirdUserId = savedThirdUser.getId();

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
    @DisplayName("POST /api/followers - Soll 403 Forbidden zurückgeben, wenn ein Benutzer versucht, eine andere Person zu folgen")
    void createFollower_UnauthorizedUser_Returns403Forbidden() throws Exception {
        FollowerRequestDTO requestDTO = new FollowerRequestDTO();
        requestDTO.setFollowerId(testUserId);
        requestDTO.setFollowedId(otherUserId);

        mockMvc.perform(post("/api/followers")
                        .header("Authorization", "Bearer " + otherUserAuthToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }



    @Test
    @DisplayName("DELETE /api/followers/{userId}/{unfollowedId} - Soll 403 Forbidden zurückgeben, wenn ein anderer Benutzer versucht, zu entfolgen")
    void deleteFollower_UnauthorizedUser_Returns403Forbidden() throws Exception {
        // HIER WURDE DER KONSTRUKTOR-AUFRUF KORRIGIERT:
        followerRepository.save(new Follower(testUserId, new User(testUserId), new User(otherUserId)));

        mockMvc.perform(delete("/api/followers/{userId}/{unfollowedId}", testUserId, otherUserId)
                        .header("Authorization", "Bearer " + otherUserAuthToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/followers/{id} - Soll alle Follower eines Benutzers zurückgeben")
    void getAllFollowers_shouldSucceed() throws Exception {
        // HIER WURDEN DIE KONSTRUKTOR-AUFRUFE KORRIGIERT:
        followerRepository.save(new Follower(testUserId, new User(testUserId), new User(otherUserId)));
        followerRepository.save(new Follower(thirdUserId, new User(thirdUserId), new User(otherUserId)));

        mockMvc.perform(get("/api/followers/{id}", otherUserId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseDTOList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.userResponseDTOList[0].id", is(testUserId.intValue())))
                .andExpect(jsonPath("$._embedded.userResponseDTOList[1].id", is(thirdUserId.intValue())));
    }

    @Test
    @DisplayName("GET /api/followers/followed/{id} - Soll alle gefolgten Benutzer zurückgeben")
    void getAllFollowedUsers_shouldSucceed() throws Exception {
        // HIER WURDEN DIE KONSTRUKTOR-AUFRUFE KORRIGIERT:
        followerRepository.save(new Follower(testUserId, new User(testUserId), new User(otherUserId)));
        followerRepository.save(new Follower(testUserId, new User(testUserId), new User(thirdUserId)));

        mockMvc.perform(get("/api/followers/followed/{id}", testUserId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseDTOList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.userResponseDTOList[0].id", is(otherUserId.intValue())))
                .andExpect(jsonPath("$._embedded.userResponseDTOList[1].id", is(thirdUserId.intValue())));
    }


}