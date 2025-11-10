package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.LikedBookEntity;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.id.LikedBookId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikedBookPersistenceMapper {

    private final UserPersistenceMapper userMapper;
    private final BookPersistenceMapper bookMapper;

    public LikedBook toDomain(LikedBookEntity entity) {
        if (entity == null) return null;

        return new LikedBook(
                entity.getBook().getId(),
                userMapper.toDomain(entity.getUser()),
                bookMapper.toDomain(entity.getBook())
        );
    }

    public LikedBookEntity toEntity(LikedBook domain) {
        if (domain == null) return null;

        LikedBookEntity entity = new LikedBookEntity();
        entity.setId(new LikedBookId(
                domain.getUser().getId(),
                domain.getBook().getId()
        ));
        entity.setUser(userMapper.toEntity(domain.getUser()));
        entity.setBook(bookMapper.toEntity(domain.getBook()));

        return entity;
    }
}
