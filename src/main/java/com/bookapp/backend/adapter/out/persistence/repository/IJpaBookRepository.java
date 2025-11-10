package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJpaBookRepository extends IJpaBaseRepository<BookEntity, Long> {
    Page<BookEntity> findAll(Pageable pageable);

    @Query("""
                SELECT b FROM BookEntity b
                WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
                  AND (:genres IS NULL OR b.genre IN :genres)
                  AND (:languages IS NULL OR b.language IN :languages)
            """)
    Page<BookEntity> searchBooks(
            @Param("title") String title,
            @Param("genres") List<String> genres,
            @Param("languages") List<String> languages,
            Pageable pageable
    );

    @Query("SELECT count(b) FROM BookEntity b")
    long count();

    @Query("SELECT distinct(b.genre) from BookEntity b")
    List<String> findAllGenres();

    @Query("SELECT distinct(b.language) from BookEntity b")
    List<String> findAllLanguages();

    @Query("""
            SELECT b
            FROM BookEntity b
            LEFT JOIN CommentEntity c ON c.book = b
            GROUP BY b
            ORDER BY AVG(c.score) DESC
            LIMIT 12
            """)
    List<BookEntity> findHighlyRatedBooks();

    @Query("SELECT b FROM BookEntity b ORDER BY function('RANDOM') LIMIT 12")
    List<BookEntity> findBooksRandomly();

    @Query("""
            SELECT b
            FROM BookEntity b
            JOIN BooksStatusEntity bs ON bs.book = b
            JOIN FollowerEntity f ON f.followedPerson = bs.user
            WHERE f.follower.id = :userId
            GROUP BY b
            ORDER BY COUNT(bs.id) DESC
            LIMIT 12
            """)
    List<BookEntity> findPopularBooksAmongFollowed(@Param("userId") Long userId);


}
