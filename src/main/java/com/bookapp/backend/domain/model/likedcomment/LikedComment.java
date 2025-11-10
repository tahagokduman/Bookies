package com.bookapp.backend.domain.model.likedcomment;

import com.bookapp.backend.adapter.out.persistence.entity.CommentEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedComment extends BaseModel {

    private User user;

    private Comment comment;
}
