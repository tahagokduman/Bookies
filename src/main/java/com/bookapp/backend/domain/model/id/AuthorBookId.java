package com.bookapp.backend.domain.model.id;

import com.bookapp.backend.domain.model.base.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AuthorBookId extends BaseModel implements Serializable {
    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "book_id")
    private Long bookId;
}
