package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.AvatarEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.AvatarPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaAvatarRepository;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AvatarRepositoryAdapter implements IAvatarRepository {
    private final IJpaAvatarRepository repository;
    private final AvatarPersistenceMapper mapper;
    @Override
    public List<Avatar> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Avatar> findById(Long aLong) {
        return repository.findById(aLong).map(mapper::toDomain);
    }

    @Override
    public Avatar save(Avatar entity) {
        AvatarEntity savedEntity = repository.save(mapper.toEntity(entity));
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    @Override
    public Avatar update(Avatar dto, Long aLong) {
        dto.setId(aLong);
        AvatarEntity updatedEntity = repository.save(mapper.toEntity(dto));
        return mapper.toDomain(updatedEntity);
    }
}
