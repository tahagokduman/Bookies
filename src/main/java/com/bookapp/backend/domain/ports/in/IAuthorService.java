package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.book.Author;
import org.springframework.data.domain.Page;

public interface IAuthorService extends IBaseService<Author, Long> {
    Page<Author> searchAuthor(String name, int page, int size);

}
