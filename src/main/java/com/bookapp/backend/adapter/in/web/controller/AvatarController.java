package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.avatar.AvatarUpdateDto;
import com.bookapp.backend.adapter.in.web.dto.response.avatar.AvatarResponseDto;
import com.bookapp.backend.adapter.in.web.mapper.AvatarWebMapper;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.in.IAvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Avatar", description = "Avatar-related operations")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/avatars")
public class AvatarController {

    private final IAvatarService avatarService;
    private final AvatarWebMapper avatarWebMapper;

    @Operation(summary = "Get avatar by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar found successfully"),
            @ApiResponse(responseCode = "404", description = "Avatar not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AvatarResponseDto> getAvatarById(@PathVariable Long id) {
        Avatar avatar = avatarService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avatar with id " + id + " not found"));

        AvatarResponseDto dto = avatarWebMapper.toResponseDto(avatar);
        dto.add(linkTo(methodOn(AvatarController.class).getAvatarById(id)).withSelfRel());

        return ResponseEntity.ok(dto);
    }
}
