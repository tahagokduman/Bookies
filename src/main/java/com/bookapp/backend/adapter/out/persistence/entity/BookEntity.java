package com.bookapp.backend.adapter.out.persistence.entity;


import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)


public class BookEntity extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(name = "publisher")
    private String publisher;

    @Column
    private String genre;

    @Column
    private String language;

    @Column(length = 10000)
    @Size(max = 10000)
    private String description;

    @Column(length = 1000)
    @Size(max = 1000)
    private String coverImageUrl;

    private Integer pageCount;

    private Integer publishedYear;

    private String author;

}
