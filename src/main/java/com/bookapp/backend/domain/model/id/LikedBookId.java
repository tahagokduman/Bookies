package com.bookapp.backend.domain.model.id;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class LikedBookId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "book_id")
    private Long bookId;
}