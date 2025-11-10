package com.bookapp.backend.adapter.in.web.dto.request.list;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooksInListRemoveRequestDTO {

    @NotNull(message = "Listen-ID darf nicht null sein")
    private Long listId;

    @NotNull(message = "Buch-ID darf nicht null sein")
    private Long bookId;
}
