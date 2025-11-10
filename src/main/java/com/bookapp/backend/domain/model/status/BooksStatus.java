package com.bookapp.backend.domain.model.status;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.user.User;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BooksStatus extends BaseModel {
    private User user;
    private Book book;
    private Status status;
}


