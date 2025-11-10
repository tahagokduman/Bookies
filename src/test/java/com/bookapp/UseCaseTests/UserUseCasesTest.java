package com.bookapp.UseCaseTests;
import com.bookapp.backend.application.service.UserUseCases;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUseCasesTest {

    private IUserRepository userRepository;
    private UserUseCases userUseCases;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        userUseCases = new UserUseCases(userRepository);
    }

    private void mockSecurityContextWithUserId(Long userId) {
        User userDetails = mock(User.class);
        when(userDetails.getId()).thenReturn(userId);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAll() {
        User u1 = new User();
        User u2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userUseCases.getAll();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetById_found() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userUseCases.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetById_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userUseCases.getById(99L));
        assertEquals("User with id 99 was not found", ex.getMessage());
    }

    @Test
    void testCreate() {
        User user = new User();
        user.setId(1L);
        when(userRepository.save(user)).thenReturn(user);

        User result = userUseCases.create(user);

        assertEquals(1L, result.getId());
        verify(userRepository).save(user);
    }

    @Test
    void testDelete_success() {
        Long userId = 1L;
        mockSecurityContextWithUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        userUseCases.delete(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDelete_accessDenied() {
        mockSecurityContextWithUserId(2L);

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> userUseCases.delete(1L));
        assertEquals("You are not allowed to modify this user.", ex.getMessage());
    }

    @Test
    void testDelete_notFound() {
        Long userId = 1L;
        mockSecurityContextWithUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userUseCases.delete(userId));
        assertEquals("User with id 1 was not found", ex.getMessage());
    }

    @Test
    void testUpdate_success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        mockSecurityContextWithUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.update(user, userId)).thenReturn(user);

        User result = userUseCases.update(userId, user);

        assertEquals(userId, result.getId());
        verify(userRepository).update(user, userId);
    }

    @Test
    void testUpdate_accessDenied() {
        mockSecurityContextWithUserId(2L);
        User user = new User();
        user.setId(1L);

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, () -> userUseCases.update(1L, user));
        assertEquals("You are not allowed to modify this user.", ex.getMessage());
    }

    @Test
    void testUpdate_notFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        mockSecurityContextWithUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userUseCases.update(userId, user));
        assertEquals("User with id 1 was not found", ex.getMessage());
    }

    @Test
    void testGetUsersPaging() {
        Pageable pageable = PageRequest.of(0, 2);
        User u1 = new User();
        User u2 = new User();
        Page<User> page = new PageImpl<>(List.of(u1, u2));

        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userUseCases.getUsersPaging(0, 2);

        assertEquals(2, result.getContent().size());
        verify(userRepository).findAll(pageable);
    }

    @Test
    void testSearchUser() {
        Pageable pageable = PageRequest.of(0, 2);
        User u1 = new User();
        User u2 = new User();
        Page<User> page = new PageImpl<>(List.of(u1, u2));

        when(userRepository.searchUser("test", pageable)).thenReturn(page);

        Page<User> result = userUseCases.searchUser("test", 0, 2);

        assertEquals(2, result.getContent().size());
        verify(userRepository).searchUser("test", pageable);
    }
}
