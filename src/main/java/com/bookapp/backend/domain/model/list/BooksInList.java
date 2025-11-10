package com.bookapp.backend.domain.model.list;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.book.Book;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class BooksInList extends BaseModel {

    private List list;
    private Book book;
    private Integer orderInList;
}
