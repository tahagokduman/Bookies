package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;

public interface IBooksInListRepository extends IBaseRepository<BooksInList, BooksInListId> {
    java.util.List<Book> findBooksInList(Long listId);
}
