package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.follow.LikedBookRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.LikedBookResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.LikedBookWebMapper;
import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.id.LikedBookId;
import com.bookapp.backend.domain.ports.in.ILikedBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Liked Books", description = "Endpoints for liking and unliking books")
@RestController
@RequestMapping(value = "/api/liked-books", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class LikedBookController {

    private final ILikedBookService likedBookService;
    private final LikedBookWebMapper likedBookWebMapper;
    private final BookWebMapper bookWebMapper;

    @Operation(summary = "Get a liked book relation by userId and bookId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liked book found successfully"),
            @ApiResponse(responseCode = "404", description = "Liked book not found")
    })
    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<LikedBookResponseDTO> getLikedBookById(@PathVariable Long userId,
                                                                 @PathVariable Long bookId) {
        LikedBookId id = new LikedBookId(userId, bookId);
        LikedBook likedBook = likedBookService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("LikedBook not found"));

        LikedBookResponseDTO dto = likedBookWebMapper.toResponseDto(likedBook);
        dto.add(linkTo(methodOn(LikedBookController.class)
                .getLikedBookById(userId, bookId)).withSelfRel());
        dto.add(linkTo(methodOn(LikedBookController.class)
                .deleteLikedBookById(userId, bookId)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Create a new liked book relation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Liked book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<LikedBookResponseDTO> createLikedBook(
            @Valid @RequestBody LikedBookRequestDTO requestDTO) {

        LikedBook likedBook = likedBookWebMapper.toDomain(requestDTO);
        LikedBook saved = likedBookService.create(likedBook);

        LikedBookResponseDTO dto = likedBookWebMapper.toResponseDto(saved);

        LikedBookId id = new LikedBookId(
                requestDTO.getUserId(),
                requestDTO.getBookId()
        );

        dto.add(linkTo(methodOn(LikedBookController.class).getLikedBookById(id.getUserId(), id.getBookId())).withSelfRel());
        dto.add(linkTo(methodOn(LikedBookController.class).deleteLikedBookById(id.getUserId(), id.getBookId())).withRel("delete"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @Operation(summary = "Delete a liked book relation by userId and bookId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Liked book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Liked book not found")
    })
    @PreAuthorize("#userId == authentication.principal.id")
    @DeleteMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<Void> deleteLikedBookById(@PathVariable Long userId,
                                                    @PathVariable Long bookId) {
        LikedBookId id = new LikedBookId(userId, bookId);
        likedBookService.getById(id).ifPresentOrElse(
                value -> likedBookService.delete(id),
                () -> {
                    throw new EntityNotFoundException("LikedBook not found");
                }
        );
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get liked books relation by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liked book found successfully"),
            @ApiResponse(responseCode = "404", description = "Liked book not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookResponseDTO>> getLikedBooksByUserId(@PathVariable Long userId) {
        List<Book> books = likedBookService.getLikedBooksByUserId(userId);

        List<BookResponseDTO> responseBook = books.stream()
                .map(bookWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(BookController.class).getBookById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(BookController.class).updateBook(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(BookController.class).deleteBook(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseBook);
    }
}
