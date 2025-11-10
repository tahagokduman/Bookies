package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.BooksStatusEntity;
import com.bookapp.backend.domain.model.id.BooksStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IJpaBooksStatusRepository extends JpaRepository<BooksStatusEntity, BooksStatusId> {

    @Query("select u.book from BooksStatusEntity u where u.user.id = :userId and u.status.id = 1")
    List<BookEntity> getReadBooksByUserId(@Param("userId") Long id);

    @Query("select u.book from BooksStatusEntity u where u.user.id = :userId and u.status.id = 2")
    List<BookEntity> getReadListBooksByUserId(@Param("userId") Long id);
}
