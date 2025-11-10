package com.bookapp.backend.domain.model.book;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.id.AuthorBookId;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class AuthorBook extends BaseModel {
    private Author author;
    private Book book;

}
