package com.bookapp.backend.domain.model.book;

import com.bookapp.backend.domain.model.base.BaseModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Author extends BaseModel {

    private String name;
    private String description;
    private List<Book> books;

    public Author(long id, String authorName, String authorDescription, List<Book> list) {
        super(id);
        this.name = authorName;
        this.description = authorDescription;
        this.books = list;
    }

    public void updateDescription(String newDesc) {
        if (newDesc == null) {
            throw new IllegalArgumentException("Beschreibung darf nicht null sein");
        }
        this.description = newDesc;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public Author(Long id) {
        super(id);
    }

}
