package com.bookapp.backend.adapter.in.web.dto.request.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorCreateRequestDTO {

    @NotBlank(message = "Autorenname darf nicht leer sein")
    @Size(max = 100)
    private String name;

    @Size(max = 1000)
    private String description;
}
