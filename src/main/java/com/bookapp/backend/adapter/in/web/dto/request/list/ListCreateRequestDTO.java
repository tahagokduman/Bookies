package com.bookapp.backend.adapter.in.web.dto.request.list;

import com.bookapp.backend.domain.model.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ListCreateRequestDTO {

    @NotNull(message = "Listenname darf nicht null sein")
    @Size(min = 2, max = 20)
    private String name;

    @NotNull(message = "UserID darf nicht null sein")
    private Long userId;
}
