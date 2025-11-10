package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.BooksStatusEntity;
import com.bookapp.backend.domain.model.status.BooksStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BooksStatusPersistenceMapper {

    private final BookPersistenceMapper bookMapper;
    private final UserPersistenceMapper userMapper;
    private final StatusPersistenceMapper statusMapper;

    public BooksStatus toDomain(BooksStatusEntity entity) {
        if (entity == null) return null;
        BooksStatus booksStatus = new BooksStatus();
        booksStatus.setId(entity.getId());
        booksStatus.setBook(bookMapper.toDomain(entity.getBook()));
        booksStatus.setStatus(statusMapper.toDomain(entity.getStatus()));
        return booksStatus;
    }

    public BooksStatusEntity toEntity(BooksStatus domain) {
        if (domain == null) return null;

        BooksStatusEntity entity = new BooksStatusEntity();
        entity.setId(domain.getId());
        entity.setUser(userMapper.toEntity(domain.getUser()));
        entity.setBook(bookMapper.toEntity(domain.getBook()));
        entity.setStatus(statusMapper.toEntity(domain.getStatus()));
        return entity;
    }
}
