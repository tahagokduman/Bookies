package com.bookapp.backend.application.service;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.ports.in.ILikedBookService;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.ILikedBookRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedBookUseCases implements ILikedBookService {

    private final ILikedBookRepository likedBookRepository;
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;

    @Override
    public List<LikedBook> getAll() {
        return likedBookRepository.findAll();
    }

    @Override
    public Optional<LikedBook> getById(LikedBookId id) {
        return likedBookRepository.findById(id);
    }

    @Override
    public LikedBook create(LikedBook likedBook) {
        return likedBookRepository.save(likedBook);
    }

    @Override
    public void delete(LikedBookId id) {
        likedBookRepository.deleteById(id);
    }

    @Override
    public LikedBook update(LikedBookId id, LikedBook likedBook) {
        throw new UnsupportedOperationException("LikedBook kann nicht aktualisiert werden.");
    }

    @Override
    public int countByBookId(Long bookId) {
        ensureBookExist(bookId);
        return likedBookRepository.countByBookId(bookId);
    }

    @Override
    public List<Book> getLikedBooksByUserId(Long userId) {
        ensureUserExist(userId);
        return likedBookRepository.getLikedBooksByUserId(userId);
    }

    private void ensureBookExist(Long bookId){
        if (bookRepository.findById(bookId).isEmpty()) {
            throw new EntityNotFoundException("Book with id " + bookId + " was not found");
        }
    }

    private void ensureUserExist(Long userId){
        if(userRepository.findById(userId).isEmpty()){
            throw new EntityNotFoundException("User with id " + userId + " was not found");
        }
    }
}
