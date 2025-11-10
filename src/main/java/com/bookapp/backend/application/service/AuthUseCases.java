package com.bookapp.backend.application.service;

import com.bookapp.backend.adapter.out.persistence.adapter.UserRepositoryAdapter;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.model.user.UserAvatar;
import com.bookapp.backend.domain.ports.in.IAuthService;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCases implements IAuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserAvatarRepository userAvatarRepository;
    private final IAvatarRepository avatarRepository;
    @Override
    public User register(User user) {

        userRepository.findByUsername(user.getUsername()).ifPresent(existing -> {
            throw new DataIntegrityViolationException("Username already taken");
        });

        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new DataIntegrityViolationException("Email is already in use.");
        });

        User savedUser = userRepository.save(user);
        createDefaultAvatarForUser(savedUser.getId());
        return savedUser;
    }

    @Override
    public User login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + user.getUsername() + " was not found"));

        if (!passwordEncoder.matches(String.valueOf(user.getPassword()), existingUser.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return existingUser;
    }

    private void createDefaultAvatarForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id "+ userId + " was not found")
        );
        Avatar avatar = avatarRepository.findById(1L).orElseThrow(
                () -> new EntityNotFoundException("Avatar with id 1L was not found")
        );
        UserAvatar userAvatar = new UserAvatar(new UserAvatarId(userId, 1L),
                user,
                avatar
                );
        userAvatarRepository.save(userAvatar);
    }
}
