package com.bookapp.backend.adapter.in.web.dto.request.list;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BooksInListRequestDTO {

    @NotNull(message = "ListID muss zugewiesen worden sein")
    private long listId;

    @NotNull(message = "BookID muss zugewiesen worden sein")
    private long bookId;
}
