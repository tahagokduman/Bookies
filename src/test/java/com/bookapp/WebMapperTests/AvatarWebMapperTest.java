package com.bookapp.WebMapperTests;

import com.bookapp.backend.adapter.in.web.dto.request.avatar.AvatarUpdateDto;
import com.bookapp.backend.adapter.in.web.dto.response.avatar.AvatarResponseDto;
import com.bookapp.backend.adapter.in.web.mapper.AvatarWebMapper;
import com.bookapp.backend.domain.model.user.Avatar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AvatarWebMapperTest {

    private AvatarWebMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AvatarWebMapper();
    }

    @Test
    void toDomain_shouldMapFieldsCorrectly() {
        AvatarUpdateDto dto = new AvatarUpdateDto();
        dto.setAvatarId(1L);
        dto.setAvatar("testAvatar");

        Avatar avatar = mapper.toDomain(dto);

        assertThat(avatar).isNotNull();
        assertThat(avatar.getId()).isEqualTo(1L);
        assertThat(avatar.getAvatar()).isEqualTo("testAvatar");

    }

    @Test
    void toResponseDto_shouldMapFieldsCorrectly() {
        Avatar avatar = new Avatar();
        avatar.setId(2L);
        avatar.setAvatar("responseAvatar");

        AvatarResponseDto dto = mapper.toResponseDto(avatar);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getAvatar()).isEqualTo("responseAvatar");

    }
}
