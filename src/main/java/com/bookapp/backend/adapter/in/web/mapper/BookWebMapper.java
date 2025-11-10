package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.book.*;
import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.domain.model.book.Book;
import org.springframework.stereotype.Component;

@Component
public class BookWebMapper {

    public BookShortResponseDTO toShortDto(Book book) {
        BookShortResponseDTO dto = new BookShortResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        return dto;
    }


    public Book toDomain(BookCreateRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setCoverImageUrl(dto.getCoverImageUrl());
        book.setPageCount(dto.getPageCount());
        book.setPublisher(dto.getPublisher());
        book.setPublishedYear(dto.getPublishedYear());
        book.setGenre(dto.getGenre());
        book.setLanguage(dto.getLanguage());


        return book;
    }

    public Book toDomain(BookUpdateRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setDescription(dto.getDescription());
        book.setCoverImageUrl(dto.getCoverImageUrl());
        book.setPageCount(dto.getPageCount());
        book.setPublisher(dto.getPublisher());
        book.setPublishedYear(dto.getPublishedYear());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setLanguage(dto.getLanguage());

        return book;
    }

    public BookResponseDTO toResponseDto(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setDescription(book.getDescription());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setPageCount(book.getPageCount());
        dto.setPublisher(book.getPublisher());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setGenre(book.getGenre());
        dto.setLanguage(book.getLanguage());


        AuthorShortResponseDTO authorDto = new AuthorShortResponseDTO();
        authorDto.setName(book.getAuthor());
        dto.setAuthor(authorDto);

        return dto;
    }
}