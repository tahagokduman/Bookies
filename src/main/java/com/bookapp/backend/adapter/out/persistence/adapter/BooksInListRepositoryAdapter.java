package com.bookapp.backend.adapter.out.persistence.adapter;

import com.bookapp.backend.adapter.out.persistence.entity.BooksInListEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.BooksInListPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaBooksInListRepository;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;
import com.bookapp.backend.domain.ports.out.IBooksInListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BooksInListRepositoryAdapter implements IBooksInListRepository {
    private final IJpaBooksInListRepository repository;
    private final BooksInListPersistenceMapper mapper;
    private final BookPersistenceMapper bookMapper;
    @Override
    public List<BooksInList> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<BooksInList> findById(BooksInListId booksInListId) {
        return repository.findById(booksInListId).map(mapper::toDomain);
    }

    @Override
    public BooksInList save(BooksInList entity) {
        BooksInListEntity savedEntity = repository.save(mapper.toEntity(entity));
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(BooksInListId booksInListId) {
        repository.deleteById(booksInListId);
    }

    @Override
    public BooksInList update(BooksInList dto, BooksInListId booksInListId) {
        BooksInListEntity updatedEntity = repository.save(mapper.toEntity(dto));
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public List<Book> findBooksInList(Long listId) {
        return repository.findBooksByListId(listId).stream().map(bookMapper::toDomain).toList();
    }
}
