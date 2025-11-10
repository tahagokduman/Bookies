package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.status.BooksStatusRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.BooksStatusWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksStatusId;
import com.bookapp.backend.domain.ports.in.IBooksStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Books Status", description = "Endpoints for managing book statuses")
@RestController
@RequestMapping(value = "/api/books-status", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class BooksStatusController {

    private final IBooksStatusService booksService;
    private final BooksStatusWebMapper booksStatusWebMapper;
    private final BookWebMapper bookWebMapper;

    @Operation(summary = "Add a book to Read-list ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book status added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PostMapping("/read-list")
    public ResponseEntity<Void> addBookToReadList(@Valid @RequestBody BooksStatusRequestDTO requestDTO) {
        requestDTO.setStatusId(2L);
        var entity = booksStatusWebMapper.toDomain(requestDTO);
        booksService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Add a book to Read-list ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book status added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PostMapping("/read")
    public ResponseEntity<Void> addBookToReadBooks(@Valid @RequestBody BooksStatusRequestDTO requestDTO) {
        requestDTO.setStatusId(1L);
        var entity = booksStatusWebMapper.toDomain(requestDTO);
        booksService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Remove a book from read list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book status removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Entry not found")
    })
    @PreAuthorize("#userId == authentication.principal.id")
    @DeleteMapping("/read-list/user/{userId}/book/{bookId}")
    public ResponseEntity<Void> removeBookFromReadList(@PathVariable Long userId,
                                                       @PathVariable Long bookId) {

        booksService.delete(new BooksStatusId(userId, bookId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove a book from list of read books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book status removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Entry not found")
    })
    @PreAuthorize("#userId == authentication.principal.id")
    @DeleteMapping("/read/user/{userId}/book/{bookId}")
    public ResponseEntity<Void> removeBookFromReadBooks(@PathVariable Long userId,
                                                        @PathVariable Long bookId) {
        booksService.delete(new BooksStatusId(userId, bookId));
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Get books in list of will-read books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    })
    @GetMapping("/read-list/{userId}")
    public ResponseEntity<List<BookResponseDTO>> getReadListByUserId(@PathVariable Long userId){
        List<Book> books = booksService.getReadListBooksByUserId(userId);
        List<BookResponseDTO> responseList = books.stream()
                .map(bookWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(BookController.class).getBookById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(BookController.class).updateBook(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(BookController.class).deleteBook(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
    @Operation(summary = "Get books in list of read books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    })
    @GetMapping("/read/{userId}")
    public ResponseEntity<List<BookResponseDTO>> getReadBooksByUserId(@PathVariable Long userId){
        List<Book> books = booksService.getReadBooksByUserId(userId);
        List<BookResponseDTO> responseList = books.stream()
                .map(bookWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(BookController.class).getBookById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(BookController.class).updateBook(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(BookController.class).deleteBook(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
}
