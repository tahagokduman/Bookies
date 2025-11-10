package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.response.comment.CommentShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;
import com.bookapp.backend.adapter.in.web.dto.request.comment.LikedCommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.comment.LikedCommentResponseDTO;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class LikedCommentWebMapper {

    public LikedComment toDomain(LikedCommentCreateRequestDTO dto) {
        LikedComment likedComment = new LikedComment();
        likedComment.setUser(new User(dto.getUserId()));
        likedComment.setComment(new Comment(dto.getCommentId()));
        return likedComment;
    }

    public LikedCommentResponseDTO toDto(LikedComment domain) {
        LikedCommentResponseDTO dto = new LikedCommentResponseDTO();
        dto.setId(domain.getId());
        UserShortResponseDTO userDto = new UserShortResponseDTO();
        userDto.setId(domain.getUser().getId());
        userDto.setUsername(domain.getUser().getUsername());

        CommentShortResponseDTO commentDto = new CommentShortResponseDTO();
        commentDto.setId(domain.getComment().getId());
        commentDto.setContent(domain.getComment().getComment());

        dto.setUserId(userDto);
        dto.setCommentId(commentDto);
        return dto;
    }
}
