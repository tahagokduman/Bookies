package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.follow.ListFollowCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.follow.ListFollowResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.ListFollowWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.ListWebMapper;
import com.bookapp.backend.domain.model.id.ListFollowId;
import com.bookapp.backend.domain.model.listfollow.ListFollow;
import com.bookapp.backend.domain.ports.in.IListFollowService;
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

@Tag(name = "List Follows", description = "Endpoints for following and unfollowing lists")
@RestController
@RequestMapping(value = "/api/list-follows", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class ListFollowController {

    private final IListFollowService listFollowService;
    private final ListFollowWebMapper listFollowWebMapper;
    private final ListWebMapper listWebMapper;

    @Operation(summary = "Get a list follow relation by userId and listId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List follow found successfully"),
            @ApiResponse(responseCode = "404", description = "List follow not found")
    })
    @GetMapping("/user/{userId}/list/{listId}")
    public ResponseEntity<ListFollowResponseDTO> getListFollowById(@PathVariable Long userId,
                                                                   @PathVariable Long listId) {
        ListFollowId id = new ListFollowId(userId, listId);
        ListFollow listFollow = listFollowService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("ListFollow not found"));

        ListFollowResponseDTO dto = listFollowWebMapper.toResponseDto(listFollow);
        dto.add(linkTo(methodOn(ListFollowController.class).getListFollowById(userId, listId)).withSelfRel());
        dto.add(linkTo(methodOn(ListFollowController.class).deleteListFollowById(userId, listId)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new list follow relation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List follow created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<ListFollowResponseDTO> createListFollow(
            @Valid @RequestBody ListFollowCreateRequestDTO requestDTO) {

        ListFollow listFollow = listFollowWebMapper.toDomain(requestDTO);
        ListFollow saved = listFollowService.create(listFollow);

        ListFollowResponseDTO dto = listFollowWebMapper.toResponseDto(saved);
        ListFollowId id = saved.getId();

        dto.add(linkTo(methodOn(ListFollowController.class).getListFollowById(id.getUserId(), id.getListId())).withSelfRel());
        dto.add(linkTo(methodOn(ListFollowController.class).deleteListFollowById(id.getUserId(), id.getListId())).withRel("delete"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Delete a list follow relation by userId and listId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "List follow deleted successfully"),
            @ApiResponse(responseCode = "404", description = "List follow not found")
    })
    @PreAuthorize("#userId == authentication.principal.id")
    @DeleteMapping("/user/{userId}/list/{listId}")
    public ResponseEntity<Void> deleteListFollowById(@PathVariable Long userId,
                                                     @PathVariable Long listId) {
        ListFollowId id = new ListFollowId(userId, listId);
        listFollowService.getById(id).ifPresentOrElse(
                value -> listFollowService.delete(id),
                () -> {
                    throw new EntityNotFoundException("ListFollow not found");
                }
        );
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if user is following the list")
    @GetMapping("/user/{userId}/list/{listId}/is-following")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long userId,
                                               @PathVariable Long listId) {
        boolean exists = listFollowService.isFollowing(userId, listId);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Get number of followers for a list")
    @GetMapping("/list/{listId}/count")
    public ResponseEntity<Long> getFollowCount(@PathVariable Long listId) {
        Long count = listFollowService.countById_ListId(listId);
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("#dto.userId == authentication.principal.id")
    @Operation(summary = "Unfollow a list (alternative delete)")
    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollowList(@Valid @RequestBody ListFollowCreateRequestDTO dto) {
        ListFollowId id = new ListFollowId(dto.getUserId(), dto.getListId());
        listFollowService.getById(id).ifPresentOrElse(
                value -> listFollowService.delete(id),
                () -> {
                    throw new EntityNotFoundException("ListFollow not found");
                }
        );
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all followed lists by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists fetched successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ListResponseDTO>> getAllLists(@PathVariable Long userId) {
        List<com.bookapp.backend.domain.model.list.List> lists = listFollowService.getFollowedListsByUserId(userId);

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
