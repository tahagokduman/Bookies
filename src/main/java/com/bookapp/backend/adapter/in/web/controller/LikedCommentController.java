package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.response.comment.CommentResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.CommentWebMapper;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.id.LikedCommentId;
import com.bookapp.backend.domain.model.likedcomment.LikedComment;
import com.bookapp.backend.adapter.in.web.dto.request.comment.LikedCommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.mapper.LikedCommentWebMapper;
import com.bookapp.backend.domain.ports.in.ILikedCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Liked Comments", description = "Operations related to comment likes")
@RestController
@RequestMapping("/api/liked-comments")
@RequiredArgsConstructor
public class LikedCommentController {

    private final ILikedCommentService service;
    private final LikedCommentWebMapper mapper;
    private final CommentWebMapper commentMapper;

    @Operation(summary = "Like a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment liked successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    @PreAuthorize("#dto.userId == authentication.principal.id")
    public ResponseEntity<Void> likeComment(@Valid @RequestBody LikedCommentCreateRequestDTO dto) {
        LikedComment liked = mapper.toDomain(dto);
        service.create(liked);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Check if a user liked a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like status retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user or comment ID")
    })
    @GetMapping("/user/{userId}/comment/{commentId}/is-liked")
    public ResponseEntity<Boolean> isLiked(@PathVariable Long userId, @PathVariable Long commentId) {
        boolean liked = service.isCommentLiked(userId, commentId);
        return ResponseEntity.ok(liked);
    }

    @Operation(summary = "Count likes on a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like count retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment ID")
    })
    @GetMapping("/comment/{commentId}/count")
    public ResponseEntity<Long> countLikes(@PathVariable Long commentId) {
        return ResponseEntity.ok(service.countByCommentId(commentId));
    }

    @Operation(summary = "Unlike a comment by a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Like removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user or comment ID")
    })
    @PreAuthorize("@commentLikeSecurity.isCommentLikeOwner(#commentId, authentication.principal.id)")
    @DeleteMapping("/user/{userId}/comment/{commentId}")
    public ResponseEntity<Void> unlike(@PathVariable Long userId, @PathVariable("commentId") Long commentId) {
        service.delete(new LikedCommentId(userId, commentId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all liked comments by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments fetched successfully")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<CollectionModel<CommentResponseDTO>> getLikedCommentsByUserId(@PathVariable Long userId){
        List<Comment> comments = service.getLikedCommentsByUserId(userId);
        List<CommentResponseDTO> commentResponseDTOS = comments
                .stream()
                .map(commentMapper::toResponseDto)
                .toList();

        CollectionModel<CommentResponseDTO> collectionModel = CollectionModel.of(
                commentResponseDTOS,
                linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);

    }
}

