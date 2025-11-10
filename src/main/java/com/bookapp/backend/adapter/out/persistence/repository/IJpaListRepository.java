package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IJpaListRepository extends IJpaBaseRepository<ListEntity, Long>{
    @Query("select u from ListEntity u where u.user.id = :id")
    java.util.List<ListEntity> getAllListByUserId(Long id);

    @Query("SELECT lf.list  FROM ListFollowEntity lf  GROUP BY lf.list  ORDER BY COUNT(lf) DESC LIMIT 12")
    java.util.List<ListEntity> getAllListByMoreFollow();

    @Query("""
        SELECT COUNT(l)
        FROM BooksInListEntity l
        JOIN l.book b
        WHERE b.id = :bookId
    """)
    Long countListsContainingBook(Long bookId);
}
