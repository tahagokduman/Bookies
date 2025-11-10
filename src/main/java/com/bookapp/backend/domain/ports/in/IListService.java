package com.bookapp.backend.domain.ports.in;

import com.bookapp.backend.domain.model.list.List;

public interface IListService extends IBaseService<List, Long> {
    java.util.List<List> getAllListByUserId(Long id);

    java.util.List<List> exploreList();

    Long countListsContainingBook(Long bookId);

}
