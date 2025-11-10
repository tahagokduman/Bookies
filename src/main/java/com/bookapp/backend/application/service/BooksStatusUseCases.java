package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksStatusId;
import com.bookapp.backend.domain.model.status.BooksStatus;
import com.bookapp.backend.domain.ports.in.IBooksStatusService;
import com.bookapp.backend.domain.ports.out.IBooksStatusRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksStatusUseCases implements IBooksStatusService {
    private final IUserRepository userRepository;
    private final IBooksStatusRepository repository;
    @Override
    public List<BooksStatus> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<BooksStatus> getById(BooksStatusId booksStatusId) {
        return repository.findById(booksStatusId);
    }

    @Override
    public BooksStatus create(BooksStatus object) {
        return repository.save(object);
    }

    @Override
    public void delete(BooksStatusId booksStatusId) {
        repository.deleteById(booksStatusId);
    }

    @Override
    public BooksStatus update(BooksStatusId booksStatusId, BooksStatus object) {
        return repository.save(object);
    }

    @Override
    public List<Book> getReadBooksByUserId(Long userId) {
        ensureUserExist(userId);
        return repository.getReadBooksByUserId(userId);
    }

    @Override
    public List<Book> getReadListBooksByUserId(Long userId) {
        ensureUserExist(userId);
        return repository.getReadListBooksByUserId(userId);
    }

    private void ensureUserExist(Long userId){
        if (userRepository.findById(userId).isEmpty()){
            throw new EntityNotFoundException("User with id " + userId + " was not found");
        }
    }
}
