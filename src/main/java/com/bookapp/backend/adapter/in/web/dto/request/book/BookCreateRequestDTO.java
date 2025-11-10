package com.bookapp.backend.adapter.in.web.dto.request.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateRequestDTO {

    private String author;

    @NotBlank(message = "Buchtitel darf nicht leer sein")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "ISBN darf nicht leer sein")
    @Size(max = 13)
    private String isbn;

    private String description;

    private String coverImageUrl;

    private Integer pageCount;

    private String publisher;

    private Integer publishedYear;

    private String genre;

    private String language;

}
