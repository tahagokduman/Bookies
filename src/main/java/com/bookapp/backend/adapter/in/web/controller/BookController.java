package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.book.BookCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.book.BookUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.in.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Books", description = "Book-related operations")
@RestController
@RequestMapping(value = "/api/books", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {

    private final IBookService bookService;
    private final BookWebMapper bookWebMapper;

    @Operation(summary = "Get all books with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<BookResponseDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<Book> bookPage = bookService.getBooksPaging(page, size);
        List<BookResponseDTO> responseList = bookPage.getContent().stream()
                .map(bookWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(BookController.class).getBookById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(BookController.class).updateBook(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(BookController.class).deleteBook(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        CollectionModel<BookResponseDTO> collectionModel = CollectionModel.of(responseList);

        collectionModel.add(linkTo(methodOn(BookController.class)
                .getAllBooks(page, size)).withSelfRel());

        if (bookPage.hasPrevious()) {
            collectionModel.add(linkTo(methodOn(BookController.class)
                    .getAllBooks(page - 1, size)).withRel("prev"));
        }

        if (bookPage.hasNext()) {
            collectionModel.add(linkTo(methodOn(BookController.class)
                    .getAllBooks(page + 1, size)).withRel("next"));
        }

        collectionModel.add(linkTo(methodOn(BookController.class)
                .getAllBooks(0, size)).withRel("first"));

        collectionModel.add(linkTo(methodOn(BookController.class)
                .getAllBooks(bookPage.getTotalPages() - 1, size)).withRel("last"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get a book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));

        BookResponseDTO dto = bookWebMapper.toResponseDto(book);
        dto.add(linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel());
        dto.add(linkTo(methodOn(BookController.class).updateBook(id, null)).withRel("update"));
        dto.add(linkTo(methodOn(BookController.class).deleteBook(id)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookCreateRequestDTO requestDTO) {
        Book createdBook = bookService.create(bookWebMapper.toDomain(requestDTO));
        BookResponseDTO dto = bookWebMapper.toResponseDto(createdBook);

        dto.add(linkTo(methodOn(BookController.class).getBookById(createdBook.getId())).withSelfRel());
        dto.add(linkTo(methodOn(BookController.class).updateBook(createdBook.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(BookController.class).deleteBook(createdBook.getId())).withRel("delete"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Update an existing book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id,
                                                      @Valid @RequestBody BookUpdateRequestDTO requestDTO) {
        Book updatedBook = bookWebMapper.toDomain(requestDTO);
        updatedBook.setId(id); //gerekli çünkü DTO’da id yok burada sonrasında IBaseServicete fonksiyon düzenlenebilir genel hepsi için geçerli bu

        Book result = bookService.update(id, updatedBook);
        BookResponseDTO dto = bookWebMapper.toResponseDto(result);

        dto.add(linkTo(methodOn(BookController.class).getBookById(id)).withSelfRel());
        dto.add(linkTo(methodOn(BookController.class).updateBook(id, null)).withRel("update"));
        dto.add(linkTo(methodOn(BookController.class).deleteBook(id)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete a book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.getById(id).ifPresentOrElse(
                book -> bookService.delete(id),
                () -> {
                    throw new EntityNotFoundException("Book with id " + id + " not found");
                }
        );
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search books by keyword with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved based on search"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })


    @GetMapping("/search")
    public ResponseEntity<CollectionModel<BookResponseDTO>> searchBook(@RequestParam String keyword,
                                                                       @RequestParam(required = false) List<String> genres,
                                                                       @RequestParam(required = false) List<String> languages,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "12") int size) {

        Page<Book> bookPage = bookService.searchBook(keyword, genres, languages, page, size);

        List<BookResponseDTO> responseList = bookPage.getContent().stream()
                .map(bookWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(BookController.class).getBookById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(BookController.class).updateBook(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(BookController.class).deleteBook(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        CollectionModel<BookResponseDTO> collectionModel = CollectionModel.of(responseList);

        collectionModel.add(linkTo(methodOn(BookController.class)
                .searchBook(keyword, genres, languages, page, size)).withSelfRel());

        if (bookPage.hasPrevious()) {
            collectionModel.add(linkTo(methodOn(BookController.class)
                    .searchBook(keyword, genres, languages, page - 1, size)).withRel("prev"));
        }

        if (bookPage.hasNext()) {
            collectionModel.add(linkTo(methodOn(BookController.class)
                    .searchBook(keyword, genres, languages, page + 1, size)).withRel("next"));
        }

        collectionModel.add(linkTo(methodOn(BookController.class)
                .searchBook(keyword, genres, languages, 0, size)).withRel("first"));

        int lastPage = bookPage.getTotalPages() > 0 ? bookPage.getTotalPages() - 1 : 0;
        collectionModel.add(linkTo(methodOn(BookController.class)
                .searchBook(keyword, genres, languages, lastPage, size)).withRel("last"));

        return ResponseEntity.ok(collectionModel);
    }


    @Operation(summary = "Get popular books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    })
    @GetMapping("/popular")
    public ResponseEntity<List<BookResponseDTO>> getPopularBooks() {
        List<Book> books = bookService.getPopularBooks();

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

    @Operation(summary = "Filter all books by language and genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })

    @GetMapping("/genres")
    public List<String> getGenres() {
        return bookService.getGenresFromAllBooks();
    }

    @GetMapping("/languages")
    public List<String> getLanguages() {
        return bookService.getLanguagesFromAllBooks();
    }

    @Operation(summary = "Get highly rated books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    })
    @GetMapping("/highly-rated")
    public ResponseEntity<List<BookResponseDTO>> getHighlyRatedBooks() {
        List<Book> books = bookService.getHighlyRatedBooks();

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

    @Operation(summary = "Get books randomly")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    })
    @GetMapping("/random")
    public ResponseEntity<List<BookResponseDTO>> getBooksRandomly() {
        List<Book> books = bookService.getBooksRandomly();

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

    @Operation(summary = "Get popular books amoung followed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books fetched successfully")
    })
    @GetMapping("/popular/{userId}")
    public ResponseEntity<List<BookResponseDTO>> getPopularBooksAmongFollowed(@PathVariable("userId") Long userId) {
        List<Book> books = bookService.getPopularBooksAmongFollowed(userId);

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
