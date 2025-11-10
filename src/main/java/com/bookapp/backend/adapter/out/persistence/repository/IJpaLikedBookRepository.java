package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.LikedBookEntity;
import com.bookapp.backend.domain.model.id.LikedBookId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IJpaLikedBookRepository extends IJpaBaseRepository<LikedBookEntity, LikedBookId>{
    @Query("select count(u) from LikedBookEntity u where u.book.id = :id")
    int countByBookId(@Param("id") Long id);

    @Query("select u.book from LikedBookEntity u where u.user.id = :userId")
    List<BookEntity> getLikedBooksByUserId(@Param("userId") Long userId);
}
