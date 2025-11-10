package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.AvatarController;
import com.bookapp.backend.adapter.in.web.dto.response.avatar.AvatarResponseDto;
import com.bookapp.backend.adapter.in.web.mapper.AvatarWebMapper;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.in.IAvatarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvatarControllerTest {

    private MockMvc mockMvc;
    private IAvatarService avatarService;
    private AvatarWebMapper avatarWebMapper;
    private AvatarController avatarController;

    @BeforeEach
    void setUp() {
        avatarService = mock(IAvatarService.class);
        avatarWebMapper = mock(AvatarWebMapper.class);
        avatarController = new AvatarController(avatarService, avatarWebMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(avatarController)
                .setControllerAdvice(new com.bookapp.backend.application.config.GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAvatarById_shouldReturnAvatarResponse() throws Exception {
        Long avatarId = 1L;
        Avatar avatar = new Avatar();
        avatar.setId(avatarId);
        avatar.setAvatar("testAvatar");

        AvatarResponseDto responseDto = new AvatarResponseDto();
        responseDto.setId(avatarId);
        responseDto.setAvatar("testAvatar");

        when(avatarService.getById(avatarId)).thenReturn(Optional.of(avatar));
        when(avatarWebMapper.toResponseDto(avatar)).thenReturn(responseDto);

        mockMvc.perform(get("/api/avatars/{id}", avatarId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(avatarId))
                .andExpect(jsonPath("$.avatar").value("testAvatar"));

        verify(avatarService).getById(avatarId);
        verify(avatarWebMapper).toResponseDto(avatar);
    }

    @Test
    void getAvatarById_whenNotFound_shouldReturn404() throws Exception {
        Long avatarId = 999L;
        when(avatarService.getById(avatarId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/avatars/{id}", avatarId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(avatarService).getById(avatarId);
        verifyNoInteractions(avatarWebMapper);
    }
}
