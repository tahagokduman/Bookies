package com.bookapp.backend.adapter.in.web.dto.request.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateRequestDTO {

    @NotNull(message = "User-ID darf nicht null sein")
    private Long userId;

    @NotNull(message = "Book-ID darf nicht null sein")
    private Long bookId;

    @Min(value = 1, message = "Score muss mindestens 1 sein")
    @Max(value = 10, message = "Score darf maximal 10 sein")
    private int score;

    @NotBlank(message = "Kommentar darf nicht leer sein")
    private String comment;
}
