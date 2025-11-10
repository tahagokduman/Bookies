package com.bookapp.backend.domain.model.book;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.user.User;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseModel {

    private User user;
    private Book book;
    private int score;
    private String comment;

    public Comment(Long id, User user, Book book, int score, String comment) {
        super(id);
        this.user = Objects.requireNonNull(user, "Benutzer darf nicht null sein");
        this.book = Objects.requireNonNull(book, "Buch darf nicht null sein");
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("Bewertung muss zwischen 1 und 10 liegen");
        }
        this.score = score;
        this.comment = Objects.requireNonNull(comment, "Kommentar darf nicht null sein");
    }
    public Comment(Long commentId) {
        super(commentId);
    }
}
