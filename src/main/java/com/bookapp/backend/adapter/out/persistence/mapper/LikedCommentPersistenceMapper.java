package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.LikedCommentEntity;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikedCommentPersistenceMapper {

    private final UserPersistenceMapper userMapper;
    private final CommentPersistenceMapper commentMapper;

    public LikedComment toDomain(LikedCommentEntity entity) {
        if (entity == null) return null;
        LikedComment domain = new LikedComment();
        domain.setId(entity.getComment().getId());
        domain.setComment(commentMapper.toDomain(entity.getComment()));
        domain.setUser(userMapper.toDomain(entity.getUser()));
        return domain;
    }

    public LikedCommentEntity toEntity(LikedComment domain) {
        if (domain == null) return null;
        LikedCommentEntity entity = new LikedCommentEntity();
        entity.setId(new LikedCommentId(domain.getUser().getId(), domain.getComment().getId()));
        entity.setUser(userMapper.toEntity(domain.getUser()));
        entity.setComment(commentMapper.toEntity(domain.getComment()));

        return entity;
    }
}
