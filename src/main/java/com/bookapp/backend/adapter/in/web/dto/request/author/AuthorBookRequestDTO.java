package com.bookapp.backend.adapter.in.web.dto.request.author;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorBookRequestDTO {

    @NotNull(message = "Author-ID darf nicht null sein")
    private Long authorId;

    @NotNull(message = "Book-ID darf nicht null sein")
    private Long bookId;
}
