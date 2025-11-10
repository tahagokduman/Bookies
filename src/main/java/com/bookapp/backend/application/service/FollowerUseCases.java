package com.bookapp.backend.application.service;

import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.model.id.FollowerId;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IFollowerService;
import com.bookapp.backend.domain.ports.out.IFollowerRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowerUseCases implements IFollowerService {

    private final IFollowerRepository followerRepository;
    private final IUserRepository userRepository;

    @Override
    public List<Follower> getAll() {
        return followerRepository.findAll();
    }

    @Override
    public Optional<Follower> getById(FollowerId followerId) {
        return Optional.of(
                followerRepository.findById(followerId)
                        .orElseThrow(() -> new EntityNotFoundException("Follower relation with id " + followerId + " was not found"))
        );
    }

    @Override
    public Follower create(Follower follower) {
        Long followerUserId = follower.getFollower().getId();
        Long followedUserId = follower.getFollowed().getId();
        if(ensureIsFollowingRelationshipExist(followerUserId, followedUserId)){
            throw new RuntimeException(followedUserId + " already follows this user " + followedUserId);
        }
        User followerUser = userRepository.findById(followerUserId).orElseThrow(
                () -> new EntityNotFoundException("follower user couldnt find with that id : "
                + followedUserId )
        );

        User followedUser = userRepository.findById(followedUserId).orElseThrow(
                () -> new EntityNotFoundException("followed user couldnt find with that id : "
                        + followedUserId )
        );
        followerUser.setFollowersCount(new NonNegativeInteger(followerUser.getFollowersCount().getValue() + 1));
        followedUser.setFollowingCount(new NonNegativeInteger(followedUser.getFollowingCount().getValue() + 1));
                userRepository.save(followerUser);
                userRepository.save(followedUser);
        return followerRepository.save(follower);
    }

    @Override
    public void delete(FollowerId followerId) {
        Long followerUserId = followerId.getFollowerId();
        Long followedUserId = followerId.getFollowedPersonId();

       if(!ensureIsFollowingRelationshipExist(followerUserId, followedUserId)){
           throw new RuntimeException(followedUserId + " doesnt follow this user " + followedUserId);
       }

        User followerUser = userRepository.findById(followerUserId).orElseThrow(
                () -> new EntityNotFoundException("follower user couldnt find with that id : "
                        + followerUserId )
        );

        User followedUser = userRepository.findById(followedUserId).orElseThrow(
                () -> new EntityNotFoundException("followed user couldnt find with that id : "
                        + followedUserId )
        );
        followerUser.setFollowersCount(new NonNegativeInteger(followerUser.getFollowersCount().getValue() + 1));
        followedUser.setFollowingCount(new NonNegativeInteger(followedUser.getFollowingCount().getValue() + 1));
        userRepository.save(followedUser);
        userRepository.save(followedUser);
        followerRepository.deleteById(followerId);
    }

    @Override
    public Follower update(FollowerId followerId, Follower updated) {
        ensureFollowerExists(followerId);

        Long followerUserId = updated.getFollower().getId();
        Long followedUserId = updated.getFollowed().getId();

        ensureUserExists(followerUserId);
        ensureUserExists(followedUserId);

        return followerRepository.update(updated, followerId);
    }

    private void ensureUserExists(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " was not found");
        }
    }

    private void ensureFollowerExists(FollowerId followerId) {
        if (followerRepository.findById(followerId).isEmpty()) {
            throw new EntityNotFoundException("Follower relation with id " + followerId + " was not found");
        }
    }

    @Override
    public List<User> getAllFollowersByUserId(Long id) {
        return followerRepository.getAllFollowersByUserId(id);
    }

    @Override
    public List<User> getAllFollowedUsersByUserId(Long id) {
        return followerRepository.getAllFollowedUsersByUserId(id);
    }

    @Override
    public Long getFollowerCountByUserId(Long userId) {
        return followerRepository.getFollowerCountByUserId(userId);
    }

    @Override
    public Long getFollowedCountByUserId(Long userId) {
        return followerRepository.getFollowedCountByUserId(userId);
    }

    @Override
    public List<Follower> getFollowerByIds(Long followedId, Long followerId) {
        return followerRepository.getFollowerByIds(followedId, followerId);
    }
    private boolean ensureIsFollowingRelationshipExist(Long followerId, Long followedId){
        if(!followerRepository.getFollowerByIds(followerId, followedId).isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
}