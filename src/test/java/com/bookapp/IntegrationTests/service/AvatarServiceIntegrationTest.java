package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.in.IAvatarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class AvatarServiceIntegrationTest {

    @Autowired
    private IAvatarService avatarService;

    @Test
    public void testCreateAndGetById() {

        Avatar avatar = new Avatar();


        avatar.setAvatar("http://example.com/avatar.png");

        Avatar createdAvatar = avatarService.create(avatar);
        assertThat(createdAvatar).isNotNull();
        assertThat(createdAvatar.getId()).isNotNull();

        Optional<Avatar> found = avatarService.getById(createdAvatar.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getAvatar()).isEqualTo("http://example.com/avatar.png");
    }

    @Test
    public void testGetAll() {
        List<Avatar> avatars = avatarService.getAll();
        assertThat(avatars).isNotNull();
        assertThat(avatars.size()).isGreaterThanOrEqualTo(0);
    }
}
