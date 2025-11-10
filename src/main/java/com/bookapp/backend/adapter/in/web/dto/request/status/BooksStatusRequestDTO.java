package com.bookapp.backend.adapter.in.web.dto.request.status;

import com.bookapp.backend.domain.model.status.BooksStatus;
import com.bookapp.backend.domain.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BooksStatusRequestDTO {

    @NotNull(message = "BookID muss zugewiesen worden sein")
    private Long bookId;

    private Long statusId;

    @NotNull(message = "UserID muss zugewiesen worden sein")
    private Long userId;
}
