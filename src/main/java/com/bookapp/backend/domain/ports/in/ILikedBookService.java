package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.model.follower.LikedBook;

import java.util.List;

public interface ILikedBookService extends IBaseService<LikedBook, LikedBookId> {
    int countByBookId(Long bookId);
    List<Book> getLikedBooksByUserId(Long userId);
}
