package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.domain.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookRepository extends IBaseRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    Page<Book> searchBooks(String title, List<String> genres, List<String> languages, int page, int size);

    long count();

    void saveAll(List<Book> books);

    List<String> findAllGenres();

    List<String> findAllLanguages();

    List<Book> getHighlyRatedBooks();

    List<Book> getBooksRandomly();

    List<Book> getPopularBooksAmongFollowed(Long userId);

}
