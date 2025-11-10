package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;

import java.util.List;

public interface ILikedCommentService extends IBaseService<LikedComment, LikedCommentId> {
    boolean isCommentLiked(Long userId, Long commentId);

    Long countByCommentId(Long commentId);

    Long countLikedCommentsByBookId(Long bookId);

    List<Comment> getLikedCommentsByUserId(Long userId);


}