package com.bookapp.backend.domain.model.follower;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.user.User;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LikedBook extends BaseModel {

    private User user;
    private Book book;

    public LikedBook(Long id, User user, Book book) {
        super(id);
        this.user = Objects.requireNonNull(user, "Benutzer darf nicht null sein");
        this.book = Objects.requireNonNull(book, "Buch darf nicht null sein");
    }
}
