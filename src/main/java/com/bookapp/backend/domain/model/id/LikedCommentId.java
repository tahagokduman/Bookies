package com.bookapp.backend.domain.model.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable

public class LikedCommentId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "comment_id")
    private Long commentId;
}
