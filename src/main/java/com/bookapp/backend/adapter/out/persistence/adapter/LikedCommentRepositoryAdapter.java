package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.LikedCommentEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.CommentPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.LikedCommentPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaLikedCommentRepository;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;
import com.bookapp.backend.domain.ports.out.ILikedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikedCommentRepositoryAdapter implements ILikedCommentRepository {

    private final IJpaLikedCommentRepository jpaRepository;
    private final LikedCommentPersistenceMapper mapper;
    private final CommentPersistenceMapper commentMapper;

    @Override
    public boolean existsByUserIdAndCommentId(Long userId, Long commentId) {
        return jpaRepository.existsById(new LikedCommentId(userId, commentId));
    }

    @Override
    public Long countByCommentId(Long commentId) {
        return jpaRepository.countByComment_Id(commentId);
    }

    @Override
    public Long countLikedCommentsByBookId(Long bookId) {
        return jpaRepository.countLikedCommentsByBookId(bookId);
    }

    @Override
    public List<Comment> getLikedCommentsByUserId(Long userId) {
        return jpaRepository.findLikedCommentsByUserId(userId)
                .stream()
                .map(commentMapper::toDomain)
                .toList();
    }

    @Override
    public LikedComment save(LikedComment likedComment) {
        LikedCommentEntity entity = mapper.toEntity(likedComment);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(LikedCommentId id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<LikedComment> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }


    @Override
    public Optional<LikedComment> findById(LikedCommentId id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public LikedComment update(LikedComment dto, LikedCommentId id) {
        LikedCommentEntity updated = jpaRepository.save(mapper.toEntity(dto));
        return mapper.toDomain(updated);
    }
}
