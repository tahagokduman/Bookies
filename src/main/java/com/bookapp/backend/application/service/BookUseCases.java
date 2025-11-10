package com.bookapp.backend.application.service;

import com.bookapp.backend.application.config.MostPopularBooksComparator;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.in.IBookService;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookUseCases implements IBookService {

    private final IBookRepository bookRepository;
    private final MostPopularBooksComparator mostPopularBooksComparator;

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getById(Long id) {
        return Optional.of(
                bookRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " was not found"))
        );
    }

    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void delete(Long id) {
        ensureBookExists(id);
        bookRepository.deleteById(id);
    }

    @Override
    public Book update(Long id, Book book) {
        ensureBookExists(id);
        return bookRepository.update(book, id);
    }

    @Override
    public Page<Book> getBooksPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    @Override
    public Page<Book> searchBook(String keyword, List<String> genres, List<String> languages, int page, int size) {
        return bookRepository.searchBooks(keyword, genres, languages, page, size);
    }

    @Override
    public List<Book> getPopularBooks() {
        List<Book> sortedBooks = bookRepository.findAll().stream().sorted(mostPopularBooksComparator).toList();
        return sortedBooks;
    }
// TODO burası da değişti
//    @Override
//    public Page<Book> findByGenreAndLanguage(String genre, String language, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return bookRepository.findByGenreAndLanguage(genre, language, pageable);
//    }

    @Override
    public List<String> getGenresFromAllBooks() {
        return bookRepository.findAllGenres();
    }

    @Override
    public List<String> getLanguagesFromAllBooks() {
        return bookRepository.findAllLanguages();
    }

    @Override
    public List<Book> getHighlyRatedBooks() {
        return bookRepository.getHighlyRatedBooks();
    }

    @Override
    public List<Book> getBooksRandomly() {
        return bookRepository.getBooksRandomly();
    }

    @Override
    public List<Book> getPopularBooksAmongFollowed(Long userId) {
        return bookRepository.getPopularBooksAmongFollowed(userId);
    }

    private void ensureBookExists(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Book with id " + id + " was not found");
        }
    }

}
