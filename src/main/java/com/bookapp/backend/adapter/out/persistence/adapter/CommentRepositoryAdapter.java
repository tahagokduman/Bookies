package com.bookapp.backend.adapter.out.persistence.adapter;
import com.bookapp.backend.adapter.out.persistence.entity.CommentEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.CommentPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaCommentRepository;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.ICommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdapter implements ICommentRepository {

    private final IJpaCommentRepository jpaRepository;
    private final CommentPersistenceMapper commentMapper;

    @Override
    public List<Comment> findAll() {
        return jpaRepository.findAll().stream()
                .map(commentMapper::toDomain)
                .toList().reversed();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return jpaRepository.findById(id)
                .map(commentMapper::toDomain);
    }

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = commentMapper.toEntity(comment);
        CommentEntity saved = jpaRepository.save(entity);
        return commentMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Comment update(Comment dto, Long id) {
        CommentEntity comment = commentMapper.toEntity(dto);
        CommentEntity updatedComment = jpaRepository.save(comment);
        return commentMapper.toDomain(updatedComment);
    }

    private Page<Comment> convertToDomainPage(Page<CommentEntity> commentEntityPage) {
        List<Comment> bookList = commentEntityPage.getContent()
                .stream()
                .map(commentMapper::toDomain)
                .toList();

        Pageable pageable = commentEntityPage.getPageable();
        return new PageImpl<>(bookList, pageable, commentEntityPage.getTotalElements());
    }

    @Override
    public Page<Comment> findAllByBookId(Long bookId, Pageable pageable) {
        return convertToDomainPage(jpaRepository.findAllByBookId(bookId, pageable));
    }
}
