package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;

import java.util.List;

public interface IBooksInListService extends IBaseService<BooksInList, BooksInListId> {
    List<Book> findBooksInList(Long listId);


}
