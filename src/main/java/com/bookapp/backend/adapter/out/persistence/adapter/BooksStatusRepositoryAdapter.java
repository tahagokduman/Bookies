package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaBooksStatusRepository;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksStatusId;
import com.bookapp.backend.domain.model.status.BooksStatus;
import com.bookapp.backend.domain.ports.out.IBooksStatusRepository;
import com.bookapp.backend.adapter.out.persistence.entity.BooksStatusEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BooksStatusPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BooksStatusRepositoryAdapter implements IBooksStatusRepository {

    private final IJpaBooksStatusRepository iJpaBooksStatusRepository;
    private final BooksStatusPersistenceMapper booksStatusMapper;
    private final BookPersistenceMapper bookPersistenceMapper;

    @Override
    public List<BooksStatus> findAll() {
        return iJpaBooksStatusRepository
                .findAll()
                .stream()
                .map(booksStatusMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<BooksStatus> findById(BooksStatusId booksStatusId) {
        return iJpaBooksStatusRepository
                .findById(booksStatusId)
                .map(booksStatusMapper::toDomain);
    }

    @Override
    public BooksStatus save(BooksStatus entity) {
        BooksStatusEntity statusEntity = booksStatusMapper.toEntity(entity);
        BooksStatusEntity savedEntity = iJpaBooksStatusRepository.save(statusEntity);
        return booksStatusMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(BooksStatusId booksStatusId) {
        iJpaBooksStatusRepository.deleteById(booksStatusId);
    }

    @Override
    public BooksStatus update(BooksStatus dto, BooksStatusId booksStatusId) {
        BooksStatusEntity statusEntity = booksStatusMapper.toEntity(dto);
        BooksStatusEntity updatedEntity = iJpaBooksStatusRepository.save(statusEntity);
        return booksStatusMapper.toDomain(updatedEntity);
    }

    @Override
    public List<Book> getReadBooksByUserId(Long userId) {
        return iJpaBooksStatusRepository.getReadBooksByUserId(userId)
                .stream()
                .map(bookPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Book> getReadListBooksByUserId(Long userId) {
        return iJpaBooksStatusRepository.getReadListBooksByUserId(userId)
                .stream()
                .map(bookPersistenceMapper::toDomain)
                .toList();
    }
}