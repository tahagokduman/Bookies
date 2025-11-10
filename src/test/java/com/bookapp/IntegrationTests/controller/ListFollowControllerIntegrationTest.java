package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.adapter.in.web.dto.request.follow.ListFollowCreateRequestDTO;
import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.listfollow.ListFollow;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IListFollowRepository;
import com.bookapp.backend.domain.ports.out.IListRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ListFollowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IListRepository listRepository;

    @Autowired
    private IListFollowRepository listFollowRepository;

    private User testUser;
    private User otherUser;
    private List testList;

    private ListFollowId testListFollowId;
    private ListFollow testListFollow;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@test.com");
        otherUser.setPassword("password123");
        otherUser = userRepository.save(otherUser);

        testList = new List();
        testList.setUser(otherUser);
        testList.setName("Test List");
        testList = listRepository.save(testList);

        testListFollowId = new ListFollowId(testUser.getId(), testList.getId());
        testListFollow = new ListFollow();
        testListFollow.setId(testListFollowId);
        testListFollow.setUser(testUser);
        testListFollow.setList(testList);
    }

    @Test
    @DisplayName("GET /api/list-follows/user/{userId}/list/{listId} - Soll einen ListFollow zurückgeben")
    void getListFollowById_ShouldReturnListFollow() throws Exception {
        listFollowRepository.save(testListFollow);

        mockMvc.perform(get("/api/list-follows/user/{userId}/list/{listId}", testUser.getId(), testList.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.listId", is(testList.getId().intValue())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/list-follows/user/" + testUser.getId() + "/list/" + testList.getId())));
    }

    @Test
    @DisplayName("GET /api/list-follows/user/{userId}/list/{listId} - Soll 404 zurückgeben, wenn nicht gefunden")
    void getListFollowById_WhenNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/list-follows/user/{userId}/list/{listId}", testUser.getId(), testList.getId()))
                .andExpect(status().isNotFound());
    }





    @Test
    @DisplayName("GET /api/list-follows/user/{userId}/list/{listId}/is-following - Soll den Status zurückgeben")
    void isFollowing_ShouldReturnBoolean() throws Exception {
        listFollowRepository.save(testListFollow);

        mockMvc.perform(get("/api/list-follows/user/{userId}/list/{listId}/is-following", testUser.getId(), testList.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }




    @Test
    @DisplayName("GET /api/list-follows/user/{userId} - Soll alle gefolgten Listen zurückgeben")
    void getAllLists_ShouldReturnFollowedLists() throws Exception {
        List otherList = new List();
        otherList.setUser(testUser);
        otherList.setName("Other List");
        otherList = listRepository.save(otherList);

        listFollowRepository.save(testListFollow);
        ListFollow otherListFollow = new ListFollow();
        otherListFollow.setId(new ListFollowId(otherUser.getId(), otherList.getId()));
        otherListFollow.setUser(otherUser);
        otherListFollow.setList(otherList);
        listFollowRepository.save(otherListFollow);

        mockMvc.perform(get("/api/list-follows/user/{userId}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test List")));
    }
}