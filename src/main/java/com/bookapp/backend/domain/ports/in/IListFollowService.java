package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.listfollow.ListFollow;
import com.bookapp.backend.domain.model.id.ListFollowId;

public interface IListFollowService extends IBaseService<ListFollow, ListFollowId> {
    boolean isFollowing(Long userId, Long listId);

    Long countById_ListId(Long listId);

    Long countFollowersOfListsContainingBook(Long bookId);

    java.util.List<List> getFollowedListsByUserId(Long userId);

}
