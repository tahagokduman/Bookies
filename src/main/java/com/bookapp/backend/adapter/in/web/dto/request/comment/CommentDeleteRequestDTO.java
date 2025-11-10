package com.bookapp.backend.adapter.in.web.dto.request.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteRequestDTO {

    @NotNull(message = "Kommentar-ID darf nicht null sein")
    private Long commentId;

    @NotNull(message = "Benutzer-ID darf nicht null sein")
    private Long userId;
}
