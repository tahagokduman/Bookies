package com.bookapp.backend.adapter.in.web.dto.response.comment;

import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO extends RepresentationModel<CommentResponseDTO> {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookShortResponseDTO book;
    private UserShortResponseDTO user;
    private int score;
}
