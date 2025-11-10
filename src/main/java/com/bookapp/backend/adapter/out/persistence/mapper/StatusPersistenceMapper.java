package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.StatusEntity;
import com.bookapp.backend.domain.model.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusPersistenceMapper {

    private final UserPersistenceMapper userMapper;

    public Status toDomain(StatusEntity entity) {
        if (entity == null) return null;
        Status status = new Status();
        status.setId(entity.getId());
        status.setStatus(entity.getName());
        return status;
    }

    public StatusEntity toEntity(Status domain) {
        if (domain == null) return null;
        StatusEntity entity = new StatusEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getStatus());
        return entity;
    }
}
