package com.bookapp.backend.application.service;

import java.util.List;
import java.util.Optional;

import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.out.IBooksInListRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bookapp.backend.domain.ports.in.IListService;
import com.bookapp.backend.domain.ports.out.IListRepository;

import static java.util.stream.Collectors.toList;
import static org.apache.logging.log4j.ThreadContext.peek;

@Service
@RequiredArgsConstructor
public class ListUseCases implements IListService {

    private final IListRepository listRepository;
    private final IUserRepository userRepository;
    private final IBooksInListService booksInListRepository;

    @Override
    public List<com.bookapp.backend.domain.model.list.List> getAll() {
        return listRepository.findAll().stream().peek(list ->
                list.setBooksList(booksInListRepository.findBooksInList(list.getId()))).toList();
    }

    @Override
    public Optional<com.bookapp.backend.domain.model.list.List> getById(Long id) {
        return Optional.of(
                listRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("List with id " + id + " was not found"))
        );
    }

    @Override
    public com.bookapp.backend.domain.model.list.List create(com.bookapp.backend.domain.model.list.List list) {
        Long userId = list.getUser().getId();
        ensureUserExist(userId);
        return listRepository.save(list);
    }

    @Override
    public void delete(Long id) {
        ensureListExists(id);
        listRepository.deleteById(id);
    }

    @Override
    public com.bookapp.backend.domain.model.list.List update(Long id, com.bookapp.backend.domain.model.list.List list) {
        ensureListExists(id);
        Long userId = list.getUser().getId();
        ensureUserExist(userId);
        return listRepository.update(list, id);
    }


    @Override
    public List<com.bookapp.backend.domain.model.list.List> getAllListByUserId(Long id) {
        ensureUserExist(id);
        return listRepository.getAllListByUserId(id).stream().peek(d ->
                        d.setBooksList(booksInListRepository.findBooksInList(d.getId())))
                .toList();
    }

    @Override
    public List<com.bookapp.backend.domain.model.list.List> exploreList() {
        return listRepository.exploreList().stream().peek(d ->
                        d.setBooksList(booksInListRepository.findBooksInList(d.getId())))
                .toList();
    }
    @Override
    public Long countListsContainingBook(Long bookId) {
        return listRepository.countListsContainingBook(bookId);
    }

    private void ensureListExists(Long id) {
        if (listRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("List with id " + id + " was not found");
        }
    }
    private void ensureUserExist(Long userId){
        if(userRepository.findById(userId).isEmpty()){
            throw new EntityNotFoundException("User with id " + userId + " was not found");
        }
    }
}