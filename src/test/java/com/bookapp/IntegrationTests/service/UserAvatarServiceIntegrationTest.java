package com.bookapp.IntegrationTests.service;

import com.bookapp.backend.application.service.UserAvatarUseCases;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.model.user.UserAvatar;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserAvatarServiceIntegrationTest {

    @Autowired
    private UserAvatarUseCases userAvatarService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAvatarRepository avatarRepository;

    @Autowired
    private IUserAvatarRepository userAvatarRepository;

    private User testUser;
    private Avatar testAvatar;
    private UserAvatar testUserAvatar;

    @BeforeEach
    void setUp() {
        // Testdaten vorbereiten
        testUser = new User("testuser", "test@example.com", "password", null);
        testUser = userRepository.save(testUser);

        testAvatar = new Avatar("http://example.com/avatar.jpg");
        testAvatar = avatarRepository.save(testAvatar);

        testUserAvatar = new UserAvatar();
        testUserAvatar.setId(new UserAvatarId(testUser.getId(), testAvatar.getId()));
        testUserAvatar.setUser(testUser);
        testUserAvatar.setAvatar(testAvatar);
        testUserAvatar = userAvatarRepository.save(testUserAvatar);
    }

    @Test
    void getAll_ShouldReturnAllUserAvatars() {
        var result = userAvatarService.getAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testUserAvatar);
    }

    @Test
    void getById_WithValidId_ShouldReturnUserAvatar() {
        Optional<UserAvatar> result = userAvatarService.getById(testUserAvatar.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getUser()).isEqualTo(testUser);
    }

    @Test
    void getById_WithInvalidUserId_ShouldThrowException() {
        UserAvatarId invalidId = new UserAvatarId(999L, testAvatar.getId());
        assertThrows(EntityNotFoundException.class, () -> userAvatarService.getById(invalidId));
    }

    @Test
    void create_ShouldSaveNewUserAvatar() {
        User newUser = userRepository.save(new User("newuser", "new@example.com", "password", null));
        Avatar newAvatar = avatarRepository.save(new Avatar("http://new-avatar.jpg"));

        UserAvatar newUserAvatar = new UserAvatar();
        newUserAvatar.setId(new UserAvatarId(newUser.getId(), newAvatar.getId()));
        newUserAvatar.setUser(newUser);
        newUserAvatar.setAvatar(newAvatar);

        UserAvatar saved = userAvatarService.create(newUserAvatar);
        assertThat(saved.getId()).isNotNull();
        assertThat(userAvatarRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void delete_ShouldRemoveUserAvatar() {
        userAvatarService.delete(testUserAvatar.getId());
        assertThat(userAvatarRepository.findById(testUserAvatar.getId())).isEmpty();
    }

    @Test
    void update_ShouldModifyExistingUserAvatar() {
        Avatar updatedAvatar = avatarRepository.save(new Avatar("http://updated-avatar.jpg"));
        testUserAvatar.setAvatar(updatedAvatar);

        UserAvatar updated = userAvatarService.update(testUserAvatar.getId(), testUserAvatar);
        assertThat(updated.getAvatar()).isEqualTo(updatedAvatar);
    }

    @Test
    void findByUserId_ShouldReturnCorrectUserAvatar() {
        UserAvatar found = userAvatarService.findByUserId(testUser.getId());
        assertThat(found).isNotNull();
        assertThat(found.getUser()).isEqualTo(testUser);
    }

}