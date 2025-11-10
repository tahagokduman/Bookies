package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.UserAvatarEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.UserAvatarPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaUserAvatarRepository;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.UserAvatar;
import com.bookapp.backend.domain.ports.out.IUserAvatarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAvatarRepositoryAdapter implements IUserAvatarRepository {

    private final UserAvatarPersistenceMapper mapper;
    private final IJpaUserAvatarRepository repository;

    @Override
    public List<UserAvatar> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserAvatar> findById(UserAvatarId userAvatarId) {
        return repository.findById(userAvatarId).map(mapper::toDomain);
    }

    @Override
    public UserAvatar save(UserAvatar entity) {

        UserAvatarEntity userAvatar = mapper.toEntity(entity);
        UserAvatarEntity savedUserAvatar = repository.save(userAvatar);
        return mapper.toDomain(savedUserAvatar);
    }

    @Override
    public void deleteById(UserAvatarId userAvatarId) {
        repository.deleteById(userAvatarId);
    }

    @Override
    public UserAvatar update(UserAvatar dto, UserAvatarId userAvatarId) {
        UserAvatarEntity userAvatar = mapper.toEntity(dto);
        UserAvatarEntity savedUserAvatar = repository.save(userAvatar);
        return mapper.toDomain(savedUserAvatar);
    }

    @Override
    public UserAvatar findByUserId(Long userId) {
        UserAvatarEntity userAvatarEntity = repository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("UserAvatar with that user id " + userId + " was not found")
        );
        return mapper.toDomain(userAvatarEntity);
    }
}
