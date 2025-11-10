package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksStatusId;
import com.bookapp.backend.domain.model.status.BooksStatus;

import java.util.List;

public interface IBooksStatusService extends IBaseService<BooksStatus, BooksStatusId> {
    List<Book> getReadBooksByUserId(Long userId);

    List<Book> getReadListBooksByUserId(Long userId);
}
