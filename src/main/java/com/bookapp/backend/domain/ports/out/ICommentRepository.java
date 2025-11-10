package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ICommentRepository extends IBaseRepository<Comment, Long> {
    Page<Comment> findAllByBookId(Long bookId, Pageable pageable);
}

