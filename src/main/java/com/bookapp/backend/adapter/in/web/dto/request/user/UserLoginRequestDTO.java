package com.bookapp.backend.adapter.in.web.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data

public class UserLoginRequestDTO {

    @NotBlank(message = "Benutzername darf nicht leer sein")
    private String username;

    @NotBlank(message = "Passwort darf nicht leer sein")
    private String password;
}
