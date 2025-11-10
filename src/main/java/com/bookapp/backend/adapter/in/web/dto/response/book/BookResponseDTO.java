package com.bookapp.backend.adapter.in.web.dto.response.book;

import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusShortResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO extends RepresentationModel<BookResponseDTO> {
    private Long id;
    private String title;
    private String isbn;
    private String description;
    private String coverImageUrl;
    private Integer pageCount;
    private String publisher;
    private Integer publishedYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AuthorShortResponseDTO author;
    private StatusShortResponseDTO status;
    private String genre;
    private String language;

}
