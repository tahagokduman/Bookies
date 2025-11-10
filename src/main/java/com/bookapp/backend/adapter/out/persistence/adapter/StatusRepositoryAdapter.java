package com.bookapp.backend.adapter.out.persistence.adapter;
import com.bookapp.backend.adapter.out.persistence.entity.StatusEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.StatusPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaStatusRepository;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.ports.out.IStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StatusRepositoryAdapter implements IStatusRepository {

    private final IJpaStatusRepository iJpaStatusRepository;
    private final StatusPersistenceMapper statusMapper;

    @Override
    public List<Status> findAll() {
        return iJpaStatusRepository
                .findAll()
                .stream()
                .map(statusMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Status> findById(Long aLong) {
        return iJpaStatusRepository.findById(aLong).map(statusMapper::toDomain);
    }

    @Override
    public Status save(Status entity) {
        StatusEntity statusEntity = statusMapper.toEntity(entity);
        StatusEntity savedEntity = iJpaStatusRepository.save(statusEntity);
        return statusMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long aLong) {
        iJpaStatusRepository.deleteById(aLong);
    }

    @Override
    public Status update(Status dto, Long aLong) {
        StatusEntity statusEntity = statusMapper.toEntity(dto);
        StatusEntity updatedStatus = iJpaStatusRepository.save(statusEntity);
        return statusMapper.toDomain(updatedStatus);
    }

    @Override
    public boolean existsByName(String name) {
        return iJpaStatusRepository.existsByName(name);
    }

    @Override
    public Status findByName(String name) {
        return statusMapper.toDomain(iJpaStatusRepository.findByName(name));
    }
}
