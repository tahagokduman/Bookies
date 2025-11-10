package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.list.ListCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.list.ListUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.ListWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.in.IBooksInListService;
import com.bookapp.backend.domain.ports.in.IListService;
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

@Tag(name = "Lists", description = "Operations for managing user-defined book lists")
@RestController
@RequestMapping(value = "/api/lists", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class ListController {


    private final IListService listService;
    private final ListWebMapper listWebMapper;
    private final IBooksInListService booksInListService;
    private final BookWebMapper bookWebMapper;

    @Operation(summary = "Get all lists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<ListResponseDTO>> getAllLists() {
        List<com.bookapp.backend.domain.model.list.List> lists = listService.getAll();
        List<ListResponseDTO> responseList = lists.stream()
                .map(listWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(ListController.class).getListById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(ListController.class).updateList(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(ListController.class).deleteList(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "Get a list by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List fetched successfully"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ListResponseDTO> getListById(@PathVariable Long id) {
        com.bookapp.backend.domain.model.list.List list = listService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("List with id " + id + " not found"));

        ListResponseDTO dto = listWebMapper.toResponseDto(list);
        dto.setBooks(
                booksInListService
                        .findBooksInList(dto.getId())
                        .stream()
                        .map(bookWebMapper::toResponseDto)
                        .toList()
        );
        dto.add(linkTo(methodOn(ListController.class).getListById(id)).withSelfRel());
        dto.add(linkTo(methodOn(ListController.class).updateList(id, null)).withRel("update"));
        dto.add(linkTo(methodOn(ListController.class).deleteList(id)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Create a new list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<ListResponseDTO> createList(@Valid @RequestBody ListCreateRequestDTO requestDTO) {
        com.bookapp.backend.domain.model.list.List newList = listWebMapper.toDomain(requestDTO);
        com.bookapp.backend.domain.model.list.List saved = listService.create(newList);

        ListResponseDTO dto = listWebMapper.toResponseDto(saved);
        dto.add(linkTo(methodOn(ListController.class).getListById(saved.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ListController.class).updateList(saved.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(ListController.class).deleteList(saved.getId())).withRel("delete"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @Operation(summary = "Update a list by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List updated successfully"),
            @ApiResponse(responseCode = "404", description = "List not found"),
            @ApiResponse(responseCode = "400", description = "Invalid update data")
    })
    @PreAuthorize("@listSecurity.isListOwner(id, authentication.principal.id)")
    @PutMapping("/{id}")
    public ResponseEntity<ListResponseDTO> updateList(@PathVariable Long id,
                                                      @Valid @RequestBody ListUpdateRequestDTO requestDTO) {
        com.bookapp.backend.domain.model.list.List updated = listService.getById(id)
                .map(existing -> {
                    existing.rename(requestDTO.getName());
                    return listService.update(id, existing);
                })
                .orElseThrow(() -> new EntityNotFoundException("List with id " + id + " not found"));

        ListResponseDTO dto = listWebMapper.toResponseDto(updated);
        dto.add(linkTo(methodOn(ListController.class).getListById(id)).withSelfRel());
        dto.add(linkTo(methodOn(ListController.class).deleteList(id)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Delete a list by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List deleted successfully"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    @PreAuthorize("@listSecurity.isListOwner(#id, authentication.principal.id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        listService.getById(id)
                .ifPresentOrElse(
                        list -> listService.delete(id),
                        () -> {
                            throw new EntityNotFoundException("List with id " + id + " not found");
                        }
                );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ListResponseDTO>> getAllListFromUser(@PathVariable Long id) {
        List<com.bookapp.backend.domain.model.list.List> lists = listService.getAllListByUserId(id);

        List<ListResponseDTO> responseList = lists.stream()
                .map(listWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(ListController.class).getListById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(ListController.class).updateList(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(ListController.class).deleteList(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
    @Operation(summary = "Get all lists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists fetched successfully")
    })
    @GetMapping("/explore")
    public ResponseEntity<List<ListResponseDTO>> exploreList() {
        List<com.bookapp.backend.domain.model.list.List> lists = listService.exploreList();

        List<ListResponseDTO> responseList = lists.stream()
                .map(listWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(ListController.class).getListById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(ListController.class).updateList(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(ListController.class).deleteList(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
}
