package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;
import com.bookapp.backend.domain.ports.in.ILikedCommentService;
import com.bookapp.backend.domain.ports.in.INotificationService;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ILikedCommentRepository;
import com.bookapp.backend.domain.ports.out.ICommentRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedCommentUseCases implements ILikedCommentService {

    private final ILikedCommentRepository repository;
    private final INotificationService notificationService;
    private final ICommentRepository commentRepository;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;

    @Override
    public LikedComment create(LikedComment likedComment) {
        Long senderId = likedComment.getUser().getId();
        Long commentId = likedComment.getComment().getId();

        Long receiverId = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"))
                .getUser().getId();

        notificationService.createLikeCommentNotification(senderId, receiverId, commentId.toString());

        return repository.save(likedComment);
    }

    @Override
    public void delete(LikedCommentId id) {
        repository.deleteById(id);
    }

    @Override
    public LikedComment update(LikedCommentId id, LikedComment object) {
        return repository.save(object);
    }

    @Override
    public Optional<LikedComment> getById(LikedCommentId id) {
        return repository.findById(id);
    }

    @Override
    public List<LikedComment> getAll() {
        return repository.findAll();
    }

    @Override
    public boolean isCommentLiked(Long userId, Long commentId) {
        return repository.existsByUserIdAndCommentId(userId, commentId);
    }

    @Override
    public Long countByCommentId(Long commentId) {
        ensureCommentExists(commentId);
        return repository.countByCommentId(commentId);
    }

    @Override
    public Long countLikedCommentsByBookId(Long bookId) {
        ensureBookExists(bookId);
        return repository.countLikedCommentsByBookId(bookId);
    }

    @Override
    public List<Comment> getLikedCommentsByUserId(Long userId) {
        ensureUserExists(userId);
        return repository.getLikedCommentsByUserId(userId);
    }

    private void ensureCommentExists(Long commentId){
        if(commentRepository.findById(commentId).isEmpty()){
            throw new EntityNotFoundException("User with id " + commentId + " + was not found.");
        }
    }

    private void ensureBookExists(Long bookId){
        if (bookRepository.findById(bookId).isEmpty()) {
            throw new EntityNotFoundException("Book with id " + bookId + " was not found");
        }
    }

    private void ensureUserExists(Long userId){
        if(userRepository.findById(userId).isEmpty()){
            throw new EntityNotFoundException("User with id " + userId + "was not found" );
        }
    }


}
