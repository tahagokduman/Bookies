package com.bookapp.backend.adapter.in.web.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data

public class UserUpdateRequestDTO {

    @NotBlank(message = "Benutzername darf nicht leer sein")
    private String username;

    @NotBlank(message = "Email darf nicht leer sein")
    @Email(message = "Ungültige Email-Adresse")
    private String email;

    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein")
    private String password;

    @PastOrPresent(message = "Geburtsdatum muss in der Vergangenheit liegen")
    private LocalDate birthdayDate;
}
