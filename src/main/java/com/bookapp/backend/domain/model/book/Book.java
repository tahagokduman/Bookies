package com.bookapp.backend.domain.model.book;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.status.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Book extends BaseModel {
    private String author;
    private String title;
    private String isbn;
    private String description;
    private String coverImageUrl;
    private Integer pageCount;
    private String publisher;
    private Integer publishedYear;
    private String genre;
    private String language;

    public Book(String title, String isbn, String author, String description, String coverImageUrl, Integer pageCount, String publisher, Integer publishedYear, String genre, String language) {
        super();
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Buchtitel darf nicht leer sein");
        }
        this.title = title;

        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN darf nicht leer sein");
        }
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.pageCount = pageCount;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.language = language;
    }
    public Book(Long id, String title, String isbn, String author, String description, String coverImageUrl, Integer pageCount, String publisher, Integer publishedYear, String genre, String language) {
        super(id);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Buchtitel darf nicht leer sein");
        }
        this.title = title;

        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN darf nicht leer sein");
        }
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.pageCount = pageCount;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.language = language;
    }


    public Book(Long id) {
        super(id);
    }
}
