package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import com.bookapp.backend.adapter.out.persistence.entity.ListFollowEntity;
import com.bookapp.backend.domain.model.id.ListFollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJpaListFollowRepository extends JpaRepository<ListFollowEntity, ListFollowId> {
    @Query("select count(u) from ListFollowEntity u where u.list = :listId")
    Long countById_ListId(@Param("listId") Long listId);
    @Query("""
    SELECT COUNT(f)
    FROM ListFollowEntity f
    WHERE f.list IN (
        SELECT l.list
        FROM BooksInListEntity l
        WHERE l.book.id = :bookId
    )
""")
    Long countFollowersOfListsContainingBook(Long bookId);

    boolean existsById(ListFollowId id);

    @Query("select u.list from ListFollowEntity u where u.user.id = :userId")
    List<ListEntity> getFollowedListByUserId(@Param("userId") Long userId);

}
