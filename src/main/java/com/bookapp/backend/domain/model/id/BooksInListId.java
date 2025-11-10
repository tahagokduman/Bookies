package com.bookapp.backend.domain.model.id;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksInListId implements Serializable {
    @Column(name = "list_id")
    private Long listId;
    @Column(name = "book_id")
    private Long bookId;
}