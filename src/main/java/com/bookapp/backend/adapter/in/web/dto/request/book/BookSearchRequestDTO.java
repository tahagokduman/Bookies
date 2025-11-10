package com.bookapp.backend.adapter.in.web.dto.request.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchRequestDTO {

    private String title;
    private Long authorId;
    private Integer publishedYear;
//    private String isbn;
//    private String publisher;
//    private Integer minPageCount;
//    private Integer maxPageCount;
//    private String status;
//    private String sortBy;    // örn: title, publishedYear
//    private String direction; // asc, desc
//    private Integer page;
//    private Integer size;
}
