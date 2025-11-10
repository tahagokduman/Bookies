package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.list.BooksInListRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.list.BooksInListRemoveRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BooksInListWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.ListWebMapper;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.in.IListService;
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

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Books In List", description = "Endpoints for managing books in lists")
@RestController
@RequestMapping(value = "/api/books-in-list", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class BooksInListController {

    private final IBooksInListService booksInListService;
    private final BooksInListWebMapper booksInListWebMapper;
    private final IListService listService;
    private final ListWebMapper listWebMapper;

    // todo : islem yapcak kullanicinin id si kontrol edilmeli listinin user id si ile eslesmezse hata vermeli.

    @Operation(summary = "Add a book to a list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book added to list successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("@listSecurity.isListOwner(#requestDTO.listId, authentication.principal.id)")
    @PostMapping
    public ResponseEntity<Void> addBookToList(@Valid @RequestBody BooksInListRequestDTO requestDTO) {
        var entity = booksInListWebMapper.toDomain(requestDTO);
        booksInListService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Remove a book from a list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book removed from list successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Entry not found")
    })
    @PreAuthorize("@listSecurity.isListOwner(#requestDTO.listId, authentication.principal.id)")
    @DeleteMapping
    public ResponseEntity<Void> removeBookFromList(@Valid @RequestBody BooksInListRemoveRequestDTO requestDTO) {
        booksInListService.delete(new BooksInListId(requestDTO.getListId(), requestDTO.getBookId()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all lists by bookId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    })

    @GetMapping("{bookId}")
    public ResponseEntity<CollectionModel<ListResponseDTO>> getListsByBookId(@PathVariable Long bookId){
        java.util.List<List> lists = listService.getAll();
        java.util.List<ListResponseDTO> responseList = lists.stream()
                .map(listWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(ListController.class).getListById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(ListController.class).updateList(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(ListController.class).deleteList(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());
        CollectionModel<ListResponseDTO> collectionModel = CollectionModel.of(responseList);
        collectionModel.add(linkTo(methodOn(ListController.class).getAllLists()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

}
