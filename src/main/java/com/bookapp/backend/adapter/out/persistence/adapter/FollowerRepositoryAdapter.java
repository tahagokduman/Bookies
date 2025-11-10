package com.bookapp.backend.adapter.out.persistence.adapter;
import com.bookapp.backend.adapter.out.persistence.entity.FollowerEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.FollowerPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.repository.IJpaFollowerRepository;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IFollowerRepository;
import com.bookapp.backend.domain.model.id.FollowerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FollowerRepositoryAdapter implements IFollowerRepository {

    private final IJpaFollowerRepository jpaFollowerRepository;
    private final FollowerPersistenceMapper followerMapper;
    private final UserPersistenceMapper userMapper;

    @Override
    public List<Follower> findAll() {
        return jpaFollowerRepository.findAll().stream()
                .map(followerMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Follower> findById(FollowerId id) {
        return jpaFollowerRepository.findById(id)
                .map(followerMapper::toDomain);
    }

    @Override
    public Follower save(Follower follower) {
        FollowerEntity entity = followerMapper.toEntity(follower);
        FollowerEntity saved = jpaFollowerRepository.save(entity);
        return followerMapper.toDomain(saved);
    }

    @Override
    public void deleteById(FollowerId id) {
        jpaFollowerRepository.deleteById(id);
    }

    @Override
    public Follower update(Follower follower, FollowerId id) {
        FollowerEntity followerEntity = followerMapper.toEntity(follower);
        FollowerEntity updatedFollower = jpaFollowerRepository.save(followerEntity);
        return followerMapper.toDomain(updatedFollower);
    }

    @Override
    public List<User> getAllFollowersByUserId(Long id) {
        return jpaFollowerRepository.getAllFollowersByUserId(id).stream().map(userMapper::toDomain).toList();
    }

    @Override
    public List<User> getAllFollowedUsersByUserId(Long id) {
        return jpaFollowerRepository.getAllFollowedUsersByUserId(id).stream().map(userMapper::toDomain).toList();
    }

    @Override
    public Long getFollowerCountByUserId(Long userId) {
        return jpaFollowerRepository.getFollowerCountByUserId(userId);
    }

    @Override
    public Long getFollowedCountByUserId(Long userId) {
        return jpaFollowerRepository.getFollowedCountByUserId(userId);
    }

    @Override
    public List<Follower> getFollowerByIds(Long followedId, Long followerId) {
        return jpaFollowerRepository.getFollowerByIds(followedId, followerId)
                .stream()
                .map(followerMapper::toDomain)
                .toList();
    }
}
