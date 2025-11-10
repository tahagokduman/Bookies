package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface IJpaCommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByBookId(Long bookId, Pageable pageable);

}