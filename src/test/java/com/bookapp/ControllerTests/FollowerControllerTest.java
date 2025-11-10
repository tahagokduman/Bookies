package com.bookapp.ControllerTests;

import com.bookapp.backend.adapter.in.web.controller.FollowerController;
import com.bookapp.backend.adapter.in.web.dto.request.follow.FollowerRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.FollowerResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.FollowerWebMapper;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.id.FollowerId;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IFollowerService;
import com.bookapp.backend.application.service.NotificationUseCases;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowerControllerTest {

    @Mock
    private IFollowerService followerService;

    @Mock
    private FollowerWebMapper followerWebMapper;

    @Mock
    private NotificationUseCases notificationUseCases;

    @InjectMocks
    private FollowerController followerController;

    private FollowerId followerId;
    private Follower follower;
    private FollowerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        followerId = new FollowerId(1L, 2L);

        User followerUser = new User(1L);
        followerUser.setUsername("followerUser");

        User followedUser = new User(2L);
        followedUser.setUsername("followedUser");

        follower = new Follower(100L, followerUser, followedUser);
        follower.setCreatedAt(LocalDateTime.now());

        responseDTO = new FollowerResponseDTO();
        responseDTO.setId(100L);
        responseDTO.setFollower(new com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO());
        responseDTO.setFollowing(new com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO());
        responseDTO.setFollowedAt(follower.getCreatedAt());
    }

    @Test
    void testGetFollowerById_Found() {
        when(followerService.getById(followerId)).thenReturn(Optional.of(follower));
        when(followerWebMapper.toResponseDto(follower)).thenReturn(responseDTO);

        ResponseEntity<FollowerResponseDTO> response = followerController.getFollowerById(followerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(followerService).getById(followerId);
        verify(followerWebMapper).toResponseDto(follower);
    }

    @Test
    void testGetFollowerById_NotFound() {
        when(followerService.getById(followerId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            followerController.getFollowerById(followerId);
        });

        assertTrue(thrown.getMessage().contains("not found"));
    }

    @Test
    void testCreateFollower_Success() {
        FollowerRequestDTO requestDTO = new FollowerRequestDTO();
        requestDTO.setFollowerId(1L);
        requestDTO.setFollowedId(2L);

        when(followerWebMapper.toDomain(requestDTO)).thenReturn(follower);
        when(followerService.create(follower)).thenReturn(follower);
        when(followerWebMapper.toResponseDto(follower)).thenReturn(responseDTO);

        ResponseEntity<FollowerResponseDTO> response = followerController.createFollower(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(notificationUseCases).createFollowUserNotification(1L, 2L);
    }

    @Test
    void testDeleteFollowerById_Success() {
        when(followerService.getById(any(FollowerId.class))).thenReturn(Optional.of(follower));
        doNothing().when(followerService).delete(any(FollowerId.class));

        ResponseEntity<Void> response = followerController.deleteFollowerById(followerId.getFollowerId(), followerId.getFollowedPersonId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(followerService).delete(any(FollowerId.class));
    }


    @Test
    void testDeleteFollowerById_NotFound() {
        when(followerService.getById(followerId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            followerController.deleteFollowerById(followerId.getFollowerId(), followerId.getFollowedPersonId());
        });

        assertTrue(thrown.getMessage().contains("not found"));
    }
}
