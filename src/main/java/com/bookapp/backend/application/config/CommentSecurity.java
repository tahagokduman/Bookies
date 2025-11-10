package com.bookapp.backend.application.config;

import com.bookapp.backend.domain.ports.in.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("commentSecurity")
@RequiredArgsConstructor
public class CommentSecurity {

    private final ICommentService commentService;
    public boolean isCommentOwner(Long commentId, Long userId) {
        return commentService.getById(commentId)
                .map(comment -> comment.getUser().getId().equals(userId))
                .orElse(false);
    }
}
