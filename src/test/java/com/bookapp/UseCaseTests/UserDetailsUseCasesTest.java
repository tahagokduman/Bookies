package com.bookapp.UseCaseTests;

import com.bookapp.backend.application.service.UserDetailsUseCases;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsUseCasesTest {

    private IUserRepository userRepository;
    private UserDetailsUseCases userDetailsUseCases;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        userDetailsUseCases = new UserDetailsUseCases(userRepository);
    }

    @Test
    void testLoadUserByUsername_existingUser() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsUseCases.loadUserByUsername("testuser");

        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_notFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsUseCases.loadUserByUsername("unknown"));

        assertEquals("User couldnt find: unknown", exception.getMessage());
    }
}
