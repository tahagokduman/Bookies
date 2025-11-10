package com.bookapp.backend.domain.ports.out;

import java.util.List;

public interface IListRepository extends IBaseRepository<com.bookapp.backend.domain.model.list.List, Long>{
    List<com.bookapp.backend.domain.model.list.List> getAllListByUserId(Long id);

    List<com.bookapp.backend.domain.model.list.List> exploreList();

    Long countListsContainingBook(Long bookId);


}
