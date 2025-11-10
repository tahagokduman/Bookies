package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.out.IBooksInListRepository;
import com.bookapp.backend.domain.ports.out.IListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksInListUseCases implements IBooksInListService {
    private final IBooksInListRepository repository;
    private final IListRepository listRepository;
    @Override
    public List<BooksInList> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<BooksInList> getById(BooksInListId booksInListId) {
        return repository.findById(booksInListId);
    }

    @Override
    public BooksInList create(BooksInList object) {
        return repository.save(object);
    }

    @Override
    public void delete(BooksInListId booksInListId) {
        repository.deleteById(booksInListId);
    }

    @Override
    public BooksInList update(BooksInListId booksInListId, BooksInList object) {
        return repository.update(object, booksInListId);
    }

    @Override
    public List<Book> findBooksInList(Long listId) {
        ensureListExists(listId);
        return repository.findBooksInList(listId);
    }

    private void ensureListExists(Long listId){
        if(listRepository.findById(listId).isEmpty()){
            throw new EntityNotFoundException("Entity with id " + listId + " was not found");
        }
    }
}
