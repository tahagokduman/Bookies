package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.book.Comment;
import org.springframework.data.domain.Page;

public interface ICommentService extends IBaseService<Comment, Long> {
    Page<Comment> getCommentsPaging(int page, int size, Long id);
}
