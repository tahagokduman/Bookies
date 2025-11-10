package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJpaUserRepository extends IJpaBaseRepository<UserEntity, Long>{
    @Query("select u from UserEntity u where u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username")String username);
    @Query("select u from UserEntity u where u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email")String email);
    Page<UserEntity> findAll(Pageable pageable);
    @Query("SELECT b FROM UserEntity b WHERE LOWER(b.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<UserEntity> findByTitleContainingIgnoreCase(@Param("keyword")String username, Pageable pageable);
    @Modifying
    @Query("DELETE FROM UserEntity ")
    void deleteAll();

}
