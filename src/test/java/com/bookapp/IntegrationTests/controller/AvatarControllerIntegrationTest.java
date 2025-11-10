package com.bookapp.IntegrationTests.controller;

import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AvatarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IAvatarRepository avatarRepository;


    @Test
    void getAvatarById_ShouldReturn401_WhenUnauthenticated() throws Exception {
        Avatar testAvatar = avatarRepository.save(new Avatar("test_avatar_url"));

        mockMvc.perform(get("/api/avatars/{id}", testAvatar.getId()))
                .andExpect(status().isUnauthorized());
    }
}