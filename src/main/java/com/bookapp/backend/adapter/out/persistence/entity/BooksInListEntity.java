package com.bookapp.backend.adapter.out.persistence.entity;

import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.id.FollowerId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "books_in_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BooksInListEntity {

    @EmbeddedId
    private BooksInListId id;

    @MapsId("listId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", insertable = false, updatable = false)
    private ListEntity list;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private BookEntity book;
}