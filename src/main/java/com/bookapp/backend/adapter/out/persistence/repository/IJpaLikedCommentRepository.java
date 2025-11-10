package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.CommentEntity;
import com.bookapp.backend.adapter.out.persistence.entity.LikedCommentEntity;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJpaLikedCommentRepository extends JpaRepository<LikedCommentEntity, LikedCommentId> {
    @Query("select count(u) from LikedCommentEntity u where u.comment.id = :commentId")
    Long countByComment_Id(@Param("commentId") Long commentId);

    @Query("select count(u) from LikedCommentEntity u where u.comment.book.id = :bookId")
    Long countLikedCommentsByBookId(@Param("bookId") Long bookId);

    @Query("select u.comment from LikedCommentEntity u where u.user.id = :userId")
    List<CommentEntity> findLikedCommentsByUserId(@Param("userId") Long userId);

}