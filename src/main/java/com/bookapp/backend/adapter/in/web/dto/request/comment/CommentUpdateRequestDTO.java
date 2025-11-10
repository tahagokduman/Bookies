package com.bookapp.backend.adapter.in.web.dto.request.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDTO {

    @Min(value = 1, message = "Score muss mindestens 1 sein")
    @Max(value = 10, message = "Score darf maximal 10 sein")
    private int score;

    @NotBlank(message = "Kommentar darf nicht leer sein")
    private String comment;
}
