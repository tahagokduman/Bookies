package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.model.follower.LikedBook;

import java.util.List;

public interface ILikedBookRepository extends IBaseRepository<LikedBook, LikedBookId>{
    int countByBookId(Long bookId);

    List<Book> getLikedBooksByUserId(Long userId);
}
