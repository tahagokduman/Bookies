package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.comment.CommentUpdateRequestDTO;
import com.bookapp.backend.domain.model.book.Comment;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.ICommentService;
import com.bookapp.backend.adapter.in.web.dto.response.comment.CommentResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.CommentWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Comments", description = "Endpoints related to user comments on books")
@RestController
@RequestMapping(value = "/api/comments", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;
    private final CommentWebMapper commentWebMapper;

    @Operation(summary = "Get a comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));

        CommentResponseDTO response = commentWebMapper.toResponseDto(comment);
        response.add(linkTo(methodOn(CommentController.class).getCommentById(id)).withSelfRel());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get paginated comments for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<CollectionModel<CommentResponseDTO>> getCommentsByBookId(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Comment> commentPage = commentService.getCommentsPaging(page, size, bookId);

        List<CommentResponseDTO> responseList = commentPage.getContent().stream()
                .map(commentWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(CommentController.class).getCommentById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(CommentController.class).updateCommentById(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(CommentController.class).deleteCommentById(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        CollectionModel<CommentResponseDTO> collectionModel = CollectionModel.of(responseList);

        // Self link
        collectionModel.add(linkTo(methodOn(CommentController.class)
                .getCommentsByBookId(bookId, page, size)).withSelfRel());

        // Pagination links
        if (commentPage.hasPrevious()) {
            collectionModel.add(linkTo(methodOn(CommentController.class)
                    .getCommentsByBookId(bookId, page - 1, size)).withRel("prev"));
        }

        if (commentPage.hasNext()) {
            collectionModel.add(linkTo(methodOn(CommentController.class)
                    .getCommentsByBookId(bookId, page + 1, size)).withRel("next"));
        }

        collectionModel.add(linkTo(methodOn(CommentController.class)
                .getCommentsByBookId(bookId, 0, size)).withRel("first"));

        collectionModel.add(linkTo(methodOn(CommentController.class)
                .getCommentsByBookId(bookId, commentPage.getTotalPages() - 1, size)).withRel("last"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Create a new comment for a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @Valid @RequestBody CommentCreateRequestDTO createRequestDTO,
            @AuthenticationPrincipal User userDetails) {

        Comment comment = commentWebMapper.toDomain(createRequestDTO);
        Long userId = userDetails.getId();
        comment.setUser(new User(userId));

        Comment saved = commentService.create(comment);

        CommentResponseDTO response = commentWebMapper.toResponseDto(saved);

        response.add(linkTo(methodOn(CommentController.class).getCommentById(saved.getId())).withSelfRel());
        response.add(linkTo(methodOn(AuthController.class).logout()).withRel("logout"));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied (not the owner)"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@commentSecurity.isCommentOwner(#id, authentication.principal.id)")
    public ResponseEntity<CommentResponseDTO> updateCommentById(@PathVariable Long id,
                                                                @Valid @RequestBody CommentUpdateRequestDTO updateRequestDTO) {
        Comment updatedComment = commentWebMapper.toDomain(updateRequestDTO);
        updatedComment.setId(id);
        Comment updated = commentService.update(id, updatedComment);
        CommentResponseDTO response = commentWebMapper.toResponseDto(updated);

        response.add(linkTo(methodOn(CommentController.class).getCommentById(id)).withSelfRel());
        response.add(linkTo(methodOn(AuthController.class).logout()).withRel("logout"));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a comment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied (not the owner)"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@commentSecurity.isCommentOwner(#id, authentication.principal.id)")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        commentService.getById(id).ifPresentOrElse(value -> {
                    commentService.delete(id);
                },
                () -> {
                    throw new EntityNotFoundException("Comment with id " + id + " not found");
                }
        );
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<CommentResponseDTO>> getAllComments() {
        List<Comment> comments = commentService.getAll();

        List<CommentResponseDTO> commentDTOs = comments.stream()
                .map(comment -> {
                    CommentResponseDTO dto = commentWebMapper.toResponseDto(comment);
                    dto.add(linkTo(methodOn(CommentController.class).getCommentById(comment.getId())).withSelfRel());
                    return dto;
                })
                .toList();

        CollectionModel<CommentResponseDTO> collectionModel = CollectionModel.of(
                commentDTOs,
                linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
