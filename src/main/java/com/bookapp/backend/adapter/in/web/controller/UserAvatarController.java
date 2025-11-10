package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.avatar.UserAvatarRequestDto;
import com.bookapp.backend.adapter.in.web.dto.response.avatar.UserAvatarResponseDto;
import com.bookapp.backend.adapter.in.web.mapper.UserAvatarWebMapper;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.model.user.UserAvatar;
import com.bookapp.backend.domain.ports.in.IAvatarService;
import com.bookapp.backend.domain.ports.in.IUserAvatarService;
import com.bookapp.backend.domain.ports.in.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "User Avatar", description = "User-Avatar-related operations")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-avatars")
public class UserAvatarController {

    private final UserAvatarWebMapper userAvatarWebMapper;
    private final IUserAvatarService userAvatarService;
    private final IAvatarService avatarService;
    private final IUserService userService;

    @Operation(summary = "Create a new user-avatar relation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User avatar relation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<UserAvatarResponseDto> createUserAvatar(
            @Valid @RequestBody UserAvatarRequestDto requestDTO) {

        UserAvatar userAvatar = userAvatarWebMapper.toDomain(requestDTO);
        UserAvatar saved = userAvatarService.create(userAvatar);

        UserAvatarResponseDto dto = userAvatarWebMapper.toResponseDto(saved);
        dto.add(linkTo(methodOn(UserAvatarController.class).getUserAvatarById(saved.getUser().getId())).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get user-avatar relation by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User avatar relation found successfully"),
            @ApiResponse(responseCode = "404", description = "User avatar relation not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserAvatarResponseDto> getUserAvatarById(@PathVariable Long userId) {
        UserAvatar userAvatar = userAvatarService.findByUserId(userId);

        UserAvatarResponseDto dto = userAvatarWebMapper.toResponseDto(userAvatar);
        dto.add(linkTo(methodOn(UserAvatarController.class).getUserAvatarById(userId)).withSelfRel());

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Update user's avatar relation (change avatar)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User avatar updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User avatar relation not found")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<UserAvatarResponseDto> updateUserAvatar(
            @PathVariable Long userId,
            @Valid @RequestBody UserAvatarRequestDto requestDTO) {

        UserAvatar existing = userAvatarService.findByUserId(userId);

        userAvatarService.delete(existing.getId());

        UserAvatarId newId = new UserAvatarId(userId, requestDTO.getAvatarId());
        User user = userService.getById(requestDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User with id " + requestDTO.getUserId() + " was not found")
        );
        Avatar avatar = avatarService.getById(requestDTO.getAvatarId()).orElseThrow(
                () -> new EntityNotFoundException("Avatar with id " + requestDTO.getAvatarId() + " was not found")
        );
        UserAvatar newUserAvatar = new UserAvatar(newId, user, avatar);

        UserAvatar saved = userAvatarService.create(newUserAvatar);

        UserAvatarResponseDto dto = userAvatarWebMapper.toResponseDto(saved);
        dto.add(linkTo(methodOn(UserAvatarController.class).getUserAvatarById(userId)).withSelfRel());

        return ResponseEntity.ok(dto);
    }


}
