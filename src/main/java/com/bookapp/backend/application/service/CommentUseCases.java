package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.ports.in.ICommentService;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ICommentRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentUseCases implements ICommentService {

    private final ICommentRepository commentRepository;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll().reversed();
    }

    @Override
    public Optional<Comment> getById(Long id) {
        return Optional.ofNullable(commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " was not found")
                ));

    }

    @Override
    public Comment create(Comment comment) {
        Long bookId = comment.getBook().getId();
        Long userId = comment.getUser().getId();
        ensureBookExist(bookId);
        ensureUserExist(userId);

        return commentRepository.save(comment);
    }

    @Override
    public void delete(Long id) {
        ensureCommentExist(id);
        commentRepository.deleteById(id);
    }

    @Override
    public Comment update(Long id, Comment comment) {
        ensureCommentExist(id);

        Long bookId = comment.getBook().getId();
        Long userId = comment.getUser().getId();

        ensureBookExist(bookId);
        ensureUserExist(userId);

        return commentRepository.update(comment, id);
    }

    private void ensureCommentExist(Long id) {
        if (commentRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Comment with id " + id + " was not found");
        }
    }

    private void ensureBookExist(Long bookId) {
        if (bookRepository.findById(bookId).isEmpty()) {
            throw new EntityNotFoundException("Book with id " + bookId + " was not found");
        }
    }

    private void ensureUserExist(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " was not found");
        }
    }

    @Override
    public Page<Comment> getCommentsPaging(int page, int size, Long id) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findAllByBookId(id, pageable);
    }
}
