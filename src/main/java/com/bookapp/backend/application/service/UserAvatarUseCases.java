package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.UserAvatar;
import com.bookapp.backend.domain.ports.in.IUserAvatarService;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserAvatarRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAvatarUseCases implements IUserAvatarService {

    private final IUserAvatarRepository repository;
    private final IUserRepository userRepository;
    private final IAvatarRepository avatarRepository;

    @Override
    public List<UserAvatar> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserAvatar> getById(UserAvatarId userAvatarId) {
        ensureAvatarExists(userAvatarId.getAvatarId());
        ensureUserExists(userAvatarId.getUserId());
        return repository.findById(userAvatarId);
    }

    @Override
    public UserAvatar create(UserAvatar object) {
        return repository.save(object);
    }

    @Override
    public void delete(UserAvatarId userAvatarId) {
        ensureAvatarExists(userAvatarId.getAvatarId());
        ensureUserExists(userAvatarId.getUserId());
        repository.deleteById(userAvatarId);
    }

    @Override
    public UserAvatar update(UserAvatarId userAvatarId, UserAvatar object) {
        ensureAvatarExists(userAvatarId.getAvatarId());
        ensureUserExists(userAvatarId.getUserId());
        return repository.update(object, userAvatarId);
    }

    @Override
    public UserAvatar findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    private void ensureUserExists(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " was not found.");
        }
    }
    private void ensureAvatarExists(Long id) {
        if (avatarRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Avatar with id " + id + " was not found");
        }
    }

}
