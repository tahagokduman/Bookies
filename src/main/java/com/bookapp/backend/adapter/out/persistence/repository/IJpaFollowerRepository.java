package com.bookapp.backend.adapter.out.persistence.repository;

import com.bookapp.backend.adapter.out.persistence.entity.FollowerEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.domain.model.id.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IJpaFollowerRepository extends JpaRepository<FollowerEntity, FollowerId> {
    @Query("select u.follower from FollowerEntity u where u.followedPerson.id = :id")
    List<UserEntity> getAllFollowersByUserId(@Param("id") Long id);

    @Query("select u.followedPerson from FollowerEntity u where u.follower.id = :id")
    List<UserEntity> getAllFollowedUsersByUserId(@Param("id") Long id);

    @Query("select count(u) from FollowerEntity u where u.follower.id = :userId")
    Long getFollowerCountByUserId(@Param("userId") Long userId);

    @Query("select count(u) from FollowerEntity u where u.followedPerson.id = :userId")
    Long getFollowedCountByUserId(@Param("userId") Long userId);

    @Query("select u from FollowerEntity u where u.followedPerson.id = :followedId and u.follower.id = :followerId")
    List<FollowerEntity> getFollowerByIds(@Param("followedId") Long followedId,
                                          @Param("followerId") Long followerId);
}