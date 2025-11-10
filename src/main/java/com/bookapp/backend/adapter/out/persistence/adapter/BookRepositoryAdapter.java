package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaBookRepository;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookRepositoryAdapter implements IBookRepository {

    private final IJpaBookRepository iJpaBookRepository;
    private final BookPersistenceMapper bookMapper;

    @Override
    public List<Book> findAll() {
        return iJpaBookRepository
                .findAll()
                .stream()
                .map(bookMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Book> findById(Long aLong) {
        return iJpaBookRepository.findById(aLong).map(bookMapper::toDomain);
    }

    @Override
    public Book save(Book entity) {
        BookEntity bookEntity = bookMapper.toEntity(entity);
        BookEntity savedEntity = iJpaBookRepository.save(bookEntity);
        return bookMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long aLong) {
        iJpaBookRepository.deleteById(aLong);
    }

    @Override
    public Book update(Book dto, Long aLong) {
        BookEntity book = bookMapper.toEntity(dto);
        BookEntity updatedBook = iJpaBookRepository.save(book);
        return bookMapper.toDomain(updatedBook);
    }

    private Page<Book> convertToDomainPage(Page<BookEntity> bookEntityPage) {
        List<Book> bookList = bookEntityPage.getContent().stream()
                .map(bookMapper::toDomain)
                .collect(Collectors.toList());

        Pageable pageable = bookEntityPage.getPageable();
        return new PageImpl<>(bookList, pageable, bookEntityPage.getTotalElements());
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return convertToDomainPage(iJpaBookRepository.findAll(pageable));
    }

    @Override
    public Page<Book> searchBooks(String keyword, List<String> genres, List<String> languages, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return convertToDomainPage(iJpaBookRepository.searchBooks(keyword, genres, languages, pageable));
    }

    @Override
    public long count() {
        return iJpaBookRepository.count();
    }

    @Override
    public void saveAll(List<Book> books) {
        List<BookEntity> bookEntities = books.stream()
                .map(bookMapper::toEntity)
                .toList();
        iJpaBookRepository.saveAll(bookEntities);
    }

    @Override
    public List<String> findAllGenres() {
        return iJpaBookRepository.findAllGenres();
    }

    @Override
    public List<String> findAllLanguages() {
        return iJpaBookRepository.findAllLanguages();
    }

    @Override
    public List<Book> getHighlyRatedBooks() {
        return iJpaBookRepository.findHighlyRatedBooks()
                .stream()
                .map(bookMapper::toDomain)
                .toList();
    }

    @Override
    public List<Book> getBooksRandomly() {
        return iJpaBookRepository.findBooksRandomly()
                .stream()
                .map(bookMapper::toDomain)
                .toList();
    }

    @Override
    public List<Book> getPopularBooksAmongFollowed(Long userId) {
        return iJpaBookRepository.findPopularBooksAmongFollowed(userId)
                .stream()
                .map(bookMapper::toDomain)
                .toList();
    }

}
