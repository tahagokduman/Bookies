package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.domain.model.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookPersistenceMapper {

    public Book toDomain(BookEntity entity) {
        if (entity == null) return null;
        Book newBook = new Book();
        newBook.setId(entity.getId());
        newBook.setTitle(entity.getTitle());
        newBook.setIsbn(entity.getIsbn());
        newBook.setAuthor(entity.getAuthor());
        newBook.setCoverImageUrl(entity.getCoverImageUrl());
        newBook.setDescription(entity.getDescription());
        newBook.setPageCount(entity.getPageCount());
        newBook.setPublishedYear(entity.getPublishedYear());
        newBook.setPublisher(entity.getPublisher());
        newBook.setGenre(entity.getGenre());
        newBook.setLanguage(entity.getLanguage());

        return newBook;
    }

    public BookEntity toEntity(Book domain) {
        if (domain == null) return null;
        BookEntity entity = new BookEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setIsbn(domain.getIsbn());
        entity.setAuthor(domain.getAuthor());
        entity.setCoverImageUrl(domain.getCoverImageUrl());
        entity.setDescription(domain.getDescription());
        entity.setPageCount(domain.getPageCount());
        entity.setPublishedYear(domain.getPublishedYear());
        entity.setPublisher(domain.getPublisher());
        entity.setGenre(domain.getGenre());
        entity.setLanguage(domain.getLanguage());

        return entity;
    }
}

