package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.comment.CommentResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class CommentWebMapper {

    public Comment toDomain(CommentCreateRequestDTO dto) {
        Comment comment = new Comment();
        comment.setUser(new User(dto.getUserId()));
        comment.setBook(new Book(dto.getBookId()));
        comment.setScore(dto.getScore());
        comment.setComment(dto.getComment());
        return comment;
    }

    public Comment toDomain(CommentUpdateRequestDTO dto) {
        Comment comment = new Comment();
        comment.setScore(dto.getScore());
        comment.setComment(dto.getComment());
        return comment;
    }

    public CommentResponseDTO toResponseDto(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setContent(comment.getComment());
        dto.setScore(comment.getScore());

        UserShortResponseDTO userDto = new UserShortResponseDTO();
        userDto.setId(comment.getUser().getId());
        userDto.setUsername(comment.getUser().getUsername());
        dto.setUser(userDto);

        BookShortResponseDTO bookDto = new BookShortResponseDTO();
        bookDto.setId(comment.getBook().getId());
        bookDto.setTitle(comment.getBook().getTitle());
        bookDto.setIsbn(comment.getBook().getIsbn());
        bookDto.setCoverImageUrl(comment.getBook().getCoverImageUrl());
        dto.setBook(bookDto);

        return dto;
    }
}
