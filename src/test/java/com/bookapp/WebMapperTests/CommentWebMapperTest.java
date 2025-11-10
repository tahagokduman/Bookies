package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.comment.CommentResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.CommentWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentWebMapperTest {

    private final CommentWebMapper mapper = new CommentWebMapper();

    @Test
    void toDomain_fromCreateRequest_shouldMapCorrectly() {
        CommentCreateRequestDTO dto = new CommentCreateRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(2L);
        dto.setScore(9);
        dto.setComment("Nice Buch");

        Comment comment = mapper.toDomain(dto);

        assertThat(comment.getUser().getId()).isEqualTo(1L);
        assertThat(comment.getBook().getId()).isEqualTo(2L);
        assertThat(comment.getScore()).isEqualTo(9);
        assertThat(comment.getComment()).isEqualTo("Nice Buch");
    }

    @Test
    void toDomain_fromUpdateRequest_shouldMapCorrectly() {
        CommentUpdateRequestDTO dto = new CommentUpdateRequestDTO();
        dto.setScore(6);
        dto.setComment("Geändert");

        Comment comment = mapper.toDomain(dto);

        assertThat(comment.getScore()).isEqualTo(6);
        assertThat(comment.getComment()).isEqualTo("Geändert");
    }

    @Test
    void toResponseDto_shouldMapCorrectly() {
        User user = new User(1L);
        user.setUsername("max");

        Book book = new Book(2L);
        book.setTitle("Mein Buch");

        Comment comment = new Comment();
        comment.setId(10L);
        comment.setComment("Cool");
        comment.setCreatedAt(LocalDateTime.of(2024, 6, 1, 12, 0));
        comment.setUpdatedAt(LocalDateTime.of(2024, 6, 2, 13, 30));
        comment.setUser(user);
        comment.setBook(book);

        CommentResponseDTO dto = mapper.toResponseDto(comment);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getContent()).isEqualTo("Cool");
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 0));
        assertThat(dto.getUpdatedAt()).isEqualTo(LocalDateTime.of(2024, 6, 2, 13, 30));

        UserShortResponseDTO userDto = dto.getUser();
        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getUsername()).isEqualTo("max");

        BookShortResponseDTO bookDto = dto.getBook();
        assertThat(bookDto.getId()).isEqualTo(2L);
        assertThat(bookDto.getTitle()).isEqualTo("Mein Buch");
    }
}
