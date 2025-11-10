package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.status.BooksStatusRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BooksStatusResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusShortResponseDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.status.BooksStatus;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class BooksStatusWebMapper {

    public BooksStatus toDomain(BooksStatusRequestDTO dto) {
        BooksStatus entity = new BooksStatus();
        entity.setUser(new User(dto.getUserId()));
        entity.setBook(new Book(dto.getBookId()));
        entity.setStatus(new Status(dto.getStatusId()));
        return entity;
    }

    public BooksStatusResponseDTO toResponseDto(BooksStatus bs) {
        BooksStatusResponseDTO dto = new BooksStatusResponseDTO();
        dto.setId(bs.getId());
        dto.setCreatedAt(bs.getCreatedAt());
        dto.setUpdatedAt(bs.getUpdatedAt());

        BookShortResponseDTO bookDto = new BookShortResponseDTO();
        bookDto.setId(bs.getBook().getId());
        bookDto.setTitle(bs.getBook().getTitle());

        StatusShortResponseDTO statusDto = new StatusShortResponseDTO();
        statusDto.setId(bs.getStatus().getId());
        statusDto.setStatus(bs.getStatus().getStatus());

        dto.setBook(bookDto);
        dto.setStatus(statusDto);
        return dto;
    }
}
