package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.user.UserUpdateRequestDTO;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.ports.in.IUserService;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.UserWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Users", description = "User operations like read, update, delete, search")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/users", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final UserWebMapper userWebMapper;

    @Operation(summary = "Get paginated users list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseDTO>> getUsersPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<User> userPage = userService.getUsersPaging(page, size);

        List<UserResponseDTO> responseList = userPage.getContent().stream()
                .map(userWebMapper::toResponseDTO)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(UserController.class).updateUserById(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(UserController.class).deleteUserById(dto.getId())).withRel("delete"));
                })
                .toList();

        CollectionModel<UserResponseDTO> collectionModel = CollectionModel.of(responseList);

        collectionModel.add(linkTo(methodOn(UserController.class)
                .getUsersPaging(page, size)).withSelfRel());

        if (userPage.hasPrevious()) {
            collectionModel.add(linkTo(methodOn(UserController.class)
                    .getUsersPaging(page - 1, size)).withRel("prev"));
        }

        if (userPage.hasNext()) {
            collectionModel.add(linkTo(methodOn(UserController.class)
                    .getUsersPaging(page + 1, size)).withRel("next"));
        }

        collectionModel.add(linkTo(methodOn(UserController.class)
                .getUsersPaging(0, size)).withRel("first"));

        collectionModel.add(linkTo(methodOn(UserController.class)
                .getUsersPaging(userPage.getTotalPages() - 1, size)).withRel("last"));

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        UserResponseDTO response = userWebMapper.toResponseDTO(user);
        response.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).updateUserById(id, null)).withRel("update"));
        response.add(linkTo(methodOn(UserController.class).deleteUserById(id)).withRel("delete"));
        response.add(linkTo(methodOn(AuthController.class).logout()).withRel("logout"));

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(summary = "Update user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden – Not the user owner"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> updateUserById(@PathVariable Long id,
                                                          @Valid @RequestBody UserUpdateRequestDTO updateRequestDTO) {
        User updatedUser = userWebMapper.updateRequestDTOtoDomain(updateRequestDTO);
        updatedUser.setId(id);
        User updated = userService.update(id, updatedUser);
        UserResponseDTO response = userWebMapper.toResponseDTO(updated);


        response.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        response.add(linkTo(methodOn(UserController.class).deleteUserById(id)).withRel("delete"));
        response.add(linkTo(methodOn(AuthController.class).logout()).withRel("logout"));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden – Not the user owner"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.getById(id).ifPresentOrElse(value -> {
                    userService.delete(id);
                },
                () -> {
                    throw new EntityNotFoundException("User with id " + id + " not found");
                }
        );
        return ResponseEntity
                .noContent()
                .build();
    }
    @Operation(summary = "Search users by keyword")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results returned")
    })
    @GetMapping("/search/{keyword}")
    public ResponseEntity<CollectionModel<UserResponseDTO>> searchUser(@PathVariable String keyword,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "12") int size) {

        Page<User> userPage = userService.searchUser(keyword, page, size);

        List<UserResponseDTO> responseList = userPage.getContent().stream()
                .map(userWebMapper::toResponseDTO)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(UserController.class).updateUserById(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(UserController.class).deleteUserById(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        CollectionModel<UserResponseDTO> collectionModel = CollectionModel.of(responseList);

        collectionModel.add(linkTo(methodOn(UserController.class)
                .searchUser(keyword, page, size)).withSelfRel());

        if (userPage.hasPrevious()) {
            collectionModel.add(linkTo(methodOn(UserController.class)
                    .searchUser(keyword, page - 1, size)).withRel("prev"));
        }

        if (userPage.hasNext()) {
            collectionModel.add(linkTo(methodOn(UserController.class)
                    .searchUser(keyword, page + 1, size)).withRel("next"));
        }

        collectionModel.add(linkTo(methodOn(UserController.class)
                .searchUser(keyword, 0, size)).withRel("first"));

        collectionModel.add(linkTo(methodOn(UserController.class)
                .searchUser(keyword, userPage.getTotalPages() - 1, size)).withRel("last"));

        return ResponseEntity.ok(collectionModel);
    }


}

