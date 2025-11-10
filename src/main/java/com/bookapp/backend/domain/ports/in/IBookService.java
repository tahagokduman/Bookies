package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.book.Book;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface IBookService extends IBaseService<Book, Long> {
    Page<Book> getBooksPaging(int page, int size);

    Page<Book> searchBook(String keyword, List<String> genres, List<String> languages, int page, int size);

    List<Book> getPopularBooks();

    List<String> getGenresFromAllBooks();

    List<String> getLanguagesFromAllBooks();

    List<Book> getHighlyRatedBooks();

    List<Book> getBooksRandomly();

    List<Book> getPopularBooksAmongFollowed(Long userId);

}
