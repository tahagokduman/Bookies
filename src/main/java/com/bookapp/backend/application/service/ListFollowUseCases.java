package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.listfollow.ListFollow;
import com.bookapp.backend.domain.ports.in.IListFollowService;
import com.bookapp.backend.domain.ports.in.INotificationService;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import com.bookapp.backend.domain.ports.out.IListFollowRepository;
import com.bookapp.backend.domain.ports.out.IListRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListFollowUseCases implements IListFollowService {

    private final IListFollowRepository listFollowRepository;
    private final IUserRepository userRepository;
    private final IListRepository listRepository;
    private final INotificationService notificationService;

    @Override
    public List<ListFollow> getAll() {
        return listFollowRepository.findAll();
    }

    @Override
    public Optional<ListFollow> getById(ListFollowId id) {
        return listFollowRepository.findById(id);
    }

    @Override
    public ListFollow create(ListFollow listFollow) {
        Long userId = listFollow.getId().getUserId();
        Long listId = listFollow.getId().getListId();

        ensureUserExists(userId);
        ensureListExists(listId);

        ListFollow saved = listFollowRepository.save(listFollow);

        Long receiverId = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("List with id " + listId + " was not found."))
                .getUser().getId();

        notificationService.createFollowListNotification(userId, receiverId, listId.toString());

        return saved;
    }

    @Override
    public void delete(ListFollowId id) {
        ensureUserExists(id.getUserId());
        ensureListExists(id.getListId());

        listFollowRepository.deleteById(id);
    }

    @Override
    public ListFollow update(ListFollowId id, ListFollow listFollow) {
        throw new UnsupportedOperationException("Update operation is not supported for ListFollow.");
    }

    @Override
    public boolean isFollowing(Long userId, Long listId) {
        return listFollowRepository.existsByUserIdAndListId(userId, listId);
    }

    @Override
    public Long countById_ListId(Long listId) {
        return listFollowRepository.countById_ListId(listId);
    }

    @Override
    public Long countFollowersOfListsContainingBook(Long bookId) {
        return listFollowRepository.countFollowersOfListsContainingBook(bookId);
    }

    @Override
    public List<com.bookapp.backend.domain.model.list.List> getFollowedListsByUserId(Long userId) {
        ensureUserExists(userId);
        return listFollowRepository.getFollowedListsByUserId(userId);
    }

    private void ensureUserExists(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " was not found.");
        }
    }

    private void ensureListExists(Long listId) {
        if (listRepository.findById(listId).isEmpty()) {
            throw new EntityNotFoundException("List with id " + listId + " was not found.");
        }
    }

}
