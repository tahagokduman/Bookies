package com.bookapp.UseCaseTests;

import com.bookapp.backend.application.service.FollowerUseCases;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.id.FollowerId;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.out.IFollowerRepository;
import com.bookapp.backend.domain.ports.out.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerUseCasesTest {

    @Mock
    private IFollowerRepository followerRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private FollowerUseCases followerUseCases;

    private User followerUser;
    private User followedUser;
    private Follower follower;
    private FollowerId followerId;

    @BeforeEach
    void setUp() {
        followerUser = new User(1L, "follower", "follower@test.com", "pass",
                LocalDate.now(), new NonNegativeInteger(0), new NonNegativeInteger(0));
        followedUser = new User(2L, "followed", "followed@test.com", "pass",
                LocalDate.now(), new NonNegativeInteger(0), new NonNegativeInteger(0));
        followerId = new FollowerId(1L, 2L);
        follower = new Follower(1L, followerUser, followedUser);
    }

    @Test
    void getAll_ShouldReturnAllFollowers() {
        when(followerRepository.findAll()).thenReturn(List.of(follower));

        List<Follower> result = followerUseCases.getAll();

        assertEquals(1, result.size());
        verify(followerRepository).findAll();
    }

    @Test
    void getById_ShouldReturnFollower_WhenExists() {
        when(followerRepository.findById(followerId)).thenReturn(Optional.of(follower));

        Optional<Follower> result = followerUseCases.getById(followerId);

        assertTrue(result.isPresent());
        assertEquals(followerId.getFollowerId(), result.get().getFollower().getId());
        assertEquals(followerId.getFollowedPersonId(), result.get().getFollowed().getId());
    }

    @Test
    void getById_ShouldThrow_WhenNotFound() {
        when(followerRepository.findById(followerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> followerUseCases.getById(followerId));
    }

    @Test
    void create_ShouldSuccess_WhenNewRelationship() {
        when(followerRepository.getFollowerByIds(1L, 2L)).thenReturn(Collections.emptyList());
        when(userRepository.findById(1L)).thenReturn(Optional.of(followerUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(followedUser));
        when(followerRepository.save(follower)).thenReturn(follower);

        Follower result = followerUseCases.create(follower);

        assertNotNull(result);
        assertEquals(1, followerUser.getFollowersCount().getValue());
        assertEquals(1, followedUser.getFollowingCount().getValue());
        verify(userRepository, times(2)).save(any(User.class));
        verify(followerRepository).save(follower);
    }

    @Test
    void create_ShouldThrow_WhenRelationshipExists() {
        when(followerRepository.getFollowerByIds(1L, 2L)).thenReturn(List.of(follower));

        assertThrows(RuntimeException.class, () -> followerUseCases.create(follower));
    }

    @Test
    void delete_ShouldSuccess_WhenExists() {
        when(followerRepository.getFollowerByIds(1L, 2L)).thenReturn(List.of(follower));
        when(userRepository.findById(1L)).thenReturn(Optional.of(followerUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(followedUser));

        followerUseCases.delete(followerId);

        verify(followerRepository).deleteById(followerId);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void delete_ShouldThrow_WhenNotExists() {
        when(followerRepository.getFollowerByIds(1L, 2L)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> followerUseCases.delete(followerId));
    }

    @Test
    void update_ShouldSuccess_WhenExists() {
        Follower updatedFollower = new Follower(1L, followerUser, followedUser);

        when(followerRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(1L)).thenReturn(Optional.of(followerUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(followedUser));
        when(followerRepository.update(updatedFollower, followerId)).thenReturn(updatedFollower);

        Follower result = followerUseCases.update(followerId, updatedFollower);

        assertNotNull(result);
        verify(followerRepository).update(updatedFollower, followerId);
    }

    @Test
    void getAllFollowersByUserId_ShouldReturnList() {
        when(followerRepository.getAllFollowersByUserId(2L)).thenReturn(List.of(followerUser));

        List<User> result = followerUseCases.getAllFollowersByUserId(2L);

        assertEquals(1, result.size());
        assertEquals(followerUser, result.get(0));
    }

    @Test
    void getAllFollowedUsersByUserId_ShouldReturnList() {
        when(followerRepository.getAllFollowedUsersByUserId(1L)).thenReturn(List.of(followedUser));

        List<User> result = followerUseCases.getAllFollowedUsersByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(followedUser, result.get(0));
    }

    @Test
    void getFollowerCountByUserId_ShouldReturnCount() {
        when(followerRepository.getFollowerCountByUserId(2L)).thenReturn(5L);

        Long result = followerUseCases.getFollowerCountByUserId(2L);

        assertEquals(5L, result);
    }

    @Test
    void getFollowedCountByUserId_ShouldReturnCount() {
        when(followerRepository.getFollowedCountByUserId(1L)).thenReturn(3L);

        Long result = followerUseCases.getFollowedCountByUserId(1L);

        assertEquals(3L, result);
    }

    @Test
    void getFollowerByIds_ShouldReturnList() {
        when(followerRepository.getFollowerByIds(1L, 2L)) // followedId zuerst, dann followerId
                .thenReturn(List.of(follower));

        List<Follower> result = followerUseCases.getFollowerByIds(1L, 2L);

        assertEquals(1, result.size());
        assertEquals(follower, result.get(0));
    }
}