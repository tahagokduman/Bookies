package com.bookapp.UseCaseTests;

import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import com.bookapp.backend.application.service.AvatarUseCases;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvatarUseCasesTest {

    private IAvatarRepository avatarRepository;
    private IUserRepository userRepository;
    private AvatarUseCases avatarUseCases;

    @BeforeEach
    void setUp() {
        avatarRepository = mock(IAvatarRepository.class);
        userRepository = mock(IUserRepository.class);
        avatarUseCases = new AvatarUseCases(avatarRepository, userRepository);
    }

    @Test
    void create_shouldSaveAvatar() {
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        avatar.setAvatar("testAvatar");

        when(avatarRepository.save(avatar)).thenReturn(avatar);

        Avatar saved = avatarUseCases.create(avatar);

        assertNotNull(saved);
        verify(avatarRepository).save(avatar);
    }

    @Test
    void getById_shouldReturnAvatar_whenFound() {
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        avatar.setAvatar("testAvatar");

        when(avatarRepository.findById(1L)).thenReturn(Optional.of(avatar));

        Optional<Avatar> found = avatarUseCases.getById(1L);
        assertTrue(found.isPresent());
        assertEquals(avatar, found.get());
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(avatarRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> avatarUseCases.getById(1L));
    }

    @Test
    void delete_shouldCallDelete_whenAvatarExists() {
        doNothing().when(avatarRepository).deleteById(1L);

        avatarUseCases.delete(1L);

        verify(avatarRepository).deleteById(1L);
    }

    @Test
    void update_shouldUpdateAvatar() {
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        avatar.setAvatar("testAvatar");

        when(avatarRepository.update(avatar, 1L)).thenReturn(avatar);

        Avatar updated = avatarUseCases.update(1L, avatar);

        assertEquals(avatar, updated);
        verify(avatarRepository).update(avatar, 1L);
    }

    @Test
    void getAll_shouldReturnList() {
        when(avatarRepository.findAll()).thenReturn(List.of(new Avatar(), new Avatar()));

        List<Avatar> avatars = avatarUseCases.getAll();

        assertEquals(2, avatars.size());
        verify(avatarRepository).findAll();
    }
}
