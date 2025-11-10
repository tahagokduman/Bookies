package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.follow.FollowerRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.comment.CommentResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.FollowerResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.FollowerWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.UserWebMapper;
import com.bookapp.backend.application.service.NotificationUseCases;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.id.FollowerId;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IFollowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Followers", description = "Endpoints for managing user follow relations")
@RestController
@RequestMapping(value = "/api/followers", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class FollowerController {

    private final IFollowerService followerService;
    private final FollowerWebMapper followerWebMapper;
    private final NotificationUseCases notificationUseCases;
    private final UserWebMapper userWebMapper;

    @Operation(summary = "Get follower relation by composite ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Follower relation found successfully"),
            @ApiResponse(responseCode = "404", description = "Follower relation not found")
    })
    @GetMapping
    public ResponseEntity<FollowerResponseDTO> getFollowerById(@RequestBody FollowerId id) {
        Follower follower = followerService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Follower with id " + id + " not found"));

        FollowerResponseDTO dto = followerWebMapper.toResponseDto(follower);
        dto.add(linkTo(methodOn(FollowerController.class).getFollowerById(id)).withSelfRel());
        dto.add(linkTo(methodOn(FollowerController.class).deleteFollowerById(id.getFollowerId(), id.getFollowedPersonId())).withRel("delete"));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new follower relation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Follower relation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.followerId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<FollowerResponseDTO> createFollower(@Valid @RequestBody FollowerRequestDTO requestDTO) {
        Follower newFollower = followerWebMapper.toDomain(requestDTO);
        Follower saved = followerService.create(newFollower);

        notificationUseCases.createFollowUserNotification(
                saved.getFollower().getId(),
                saved.getFollowed().getId()
        );

        FollowerId id = new FollowerId(
                saved.getFollower().getId(),
                saved.getFollowed().getId()
        );

        FollowerResponseDTO dto = followerWebMapper.toResponseDto(saved);
        dto.add(linkTo(methodOn(FollowerController.class).getFollowerById(id)).withSelfRel());
        dto.add(linkTo(methodOn(FollowerController.class).deleteFollowerById(id.getFollowerId(), id.getFollowedPersonId())).withRel("delete"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Delete a follower relation by composite ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Follower relation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Follower relation not found")
    })
    @PreAuthorize("#userId == authentication.principal.id")
    @DeleteMapping("/{userId}/{unfollowedId}")
    public ResponseEntity<Void> deleteFollowerById(@PathVariable Long userId,
                                                   @PathVariable Long unfollowedId) {
        FollowerId id = new FollowerId(userId, unfollowedId);
        followerService.getById(id).ifPresentOrElse(
                value -> followerService.delete(id),
                () -> {
                    throw new EntityNotFoundException("Follower with id " + id + " not found");
                }
        );
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Get all followers by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Followers fetched successfully")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CollectionModel<UserResponseDTO>> getAllFollowers(@PathVariable Long id) {
        List<User> followers = followerService.getAllFollowersByUserId(id);

        List<UserResponseDTO> userResponseDTOS = followers.stream()
                .map(followers1 -> {
                    UserResponseDTO dto = userWebMapper.toResponseDTO(followers1);
                    dto.add(linkTo(methodOn(UserController.class).getUserById(followers1.getId())).withSelfRel());
                    return dto;
                })
                .toList();

        CollectionModel<UserResponseDTO> collectionModel = CollectionModel.of(
                userResponseDTOS,
                linkTo(methodOn(FollowerController.class).getAllFollowers(1L)).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get all followed users by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Followed fetched successfully")
    })
    @GetMapping("/followed/{id}")
    public ResponseEntity<CollectionModel<UserResponseDTO>> getAllFollowedUsers(@PathVariable Long id) {
        List<User> followers = followerService.getAllFollowedUsersByUserId(id);

        List<UserResponseDTO> userResponseDTOS = followers.stream()
                .map(followers1 -> {
                    UserResponseDTO dto = userWebMapper.toResponseDTO(followers1);
                    dto.add(linkTo(methodOn(UserController.class).getUserById(followers1.getId())).withSelfRel());
                    return dto;
                })
                .toList();

        CollectionModel<UserResponseDTO> collectionModel = CollectionModel.of(
                userResponseDTOS,
                linkTo(methodOn(FollowerController.class).getAllFollowedUsers(1L)).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }
    @Operation(summary = "Get count of followers by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count of followers fetched successfully")
    })
    @GetMapping("/{userId}/count")
    public long getFollowerCountByUserId(@PathVariable Long userId){
        return followerService.getFollowerCountByUserId(userId);
    }

    @Operation(summary = "Get count of followed by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count of followed fetched successfully")
    })
    @GetMapping("/followed/{userId}/count")
    public long getFollowedCountByUserId(@PathVariable Long userId){
        return followerService.getFollowedCountByUserId(userId);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
