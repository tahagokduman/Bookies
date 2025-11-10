package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.BooksInListEntity;
import com.bookapp.backend.domain.model.id.BooksInListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IJpaBooksInListRepository extends JpaRepository<BooksInListEntity, BooksInListId> {
    @Query("""
      SELECT bil.book
      FROM   BooksInListEntity bil
      WHERE  bil.list.id = :listId
    """)
    java.util.List<BookEntity> findBooksByListId(@Param("listId")Long listId);
}
