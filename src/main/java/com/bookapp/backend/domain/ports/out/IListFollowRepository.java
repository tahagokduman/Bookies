package com.bookapp.backend.domain.ports.out;

import com.bookapp.backend.domain.model.listfollow.ListFollow;
import com.bookapp.backend.domain.model.id.ListFollowId;

import java.util.List;

public interface IListFollowRepository extends IBaseRepository<ListFollow, ListFollowId> {
    Long countById_ListId(Long listId);

    boolean existsByUserIdAndListId(Long userId, Long listId);

    Long countFollowersOfListsContainingBook(Long bookId);

    List<com.bookapp.backend.domain.model.list.List> getFollowedListsByUserId(Long userId);
}
