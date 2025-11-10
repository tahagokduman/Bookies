package com.bookapp.backend.adapter.in.web.dto.request.follow;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikedBookRequestDTO {

    @NotNull(message = "User ID darf nicht null sein")
    private Long userId;

    @NotNull(message = "Book ID darf nicht null sein")
    private Long bookId;
}
