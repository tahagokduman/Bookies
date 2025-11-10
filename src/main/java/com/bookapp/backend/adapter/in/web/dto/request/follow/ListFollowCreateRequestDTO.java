package com.bookapp.backend.adapter.in.web.dto.request.follow;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListFollowCreateRequestDTO {

    @NotNull(message = "UserId darf nicht null sein.")
    private Long userId;

    @NotNull(message = "ListId darf nicht null sein.")
    private Long listId;
}
