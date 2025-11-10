package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.id.AuthorBookId;
import com.bookapp.backend.domain.model.book.AuthorBook;

public interface IAuthorBookRepository extends IBaseRepository<AuthorBook, AuthorBookId> {
}
