package com.bookapp.backend.application.config;

import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.ports.in.ILikedCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("commentLikeSecurity")
@RequiredArgsConstructor
public class CommentLikeSecurity {
    private final ILikedCommentService likedCommentService;

    public boolean isCommentLikeOwner(Long commentId, Long userId){
        LikedCommentId id = new LikedCommentId(userId, commentId);
        return likedCommentService.getById(id)
                .map(l -> l.getComment().getUser().getId().equals(userId))
                .orElse(false);
    }
}
