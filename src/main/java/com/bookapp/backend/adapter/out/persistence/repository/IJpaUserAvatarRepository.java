package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.UserAvatarEntity;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJpaUserAvatarRepository extends JpaRepository<UserAvatarEntity, UserAvatarId> {

    @Query("select u from UserAvatarEntity u where u.user.id = :userId")
    Optional<UserAvatarEntity> findByUserId(@Param("userId") Long userId);
}
