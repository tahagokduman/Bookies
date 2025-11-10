package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.follow.LikedBookRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.LikedBookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class LikedBookWebMapper {

    public LikedBook toDomain(LikedBookRequestDTO dto) {
        LikedBook likedBook = new LikedBook();
        likedBook.setUser(new User(dto.getUserId()));
        likedBook.setBook(new Book(dto.getBookId()));
        return likedBook;
    }

    public LikedBookResponseDTO toResponseDto(LikedBook entity) {
        LikedBookResponseDTO dto = new LikedBookResponseDTO();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        BookResponseDTO bookDto = new BookResponseDTO();
        bookDto.setId(entity.getBook().getId());
        bookDto.setTitle(entity.getBook().getTitle());
        bookDto.setCoverImageUrl(entity.getBook().getCoverImageUrl());
        bookDto.setAuthor(new AuthorShortResponseDTO(1L, entity.getBook().getAuthor()));
        bookDto.setDescription(entity.getBook().getDescription());
        bookDto.setGenre(entity.getBook().getGenre());
        bookDto.setLanguage(entity.getBook().getLanguage());
        bookDto.setIsbn(entity.getBook().getIsbn());
        bookDto.setPublishedYear(entity.getBook().getPublishedYear());
        bookDto.setPublisher(entity.getBook().getPublisher());

        UserShortResponseDTO userDto = new UserShortResponseDTO();
        userDto.setId(entity.getUser().getId());
        userDto.setUsername(entity.getUser().getUsername());

        dto.setBook(bookDto);
        dto.setUser(userDto);
        return dto;
    }
}
