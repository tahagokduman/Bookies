package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.CommentEntity;
import com.bookapp.backend.domain.model.book.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentPersistenceMapper {

    private final UserPersistenceMapper userMapper;
    private final BookPersistenceMapper bookMapper;

    public Comment toDomain(CommentEntity entity) {
        if (entity == null) return null;
        Comment comment = new Comment();
        comment.setId(entity.getId());
        comment.setUser(userMapper.toDomain(entity.getUser()));
        comment.setBook(bookMapper.toDomain(entity.getBook()));
        comment.setScore(entity.getScore());
        comment.setUpdatedAt(entity.getUpdatedAt());
        comment.setCreatedAt(entity.getCreatedAt());
        comment.setComment(entity.getComment());

        return comment;
    }

    public CommentEntity toEntity(Comment domain) {
        if (domain == null) return null;

        CommentEntity entity = new CommentEntity();
        entity.setId(domain.getId());
        entity.setUser(userMapper.toEntity(domain.getUser()));
        entity.setBook(bookMapper.toEntity(domain.getBook()));
        entity.setScore(domain.getScore());
        entity.setComment(domain.getComment());
        return entity;
    }
}
