package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.status.StatusRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.status.StatusResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.StatusWebMapper;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.ports.in.IStatusService;
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

@Tag(name = "Statuses", description = "Operations related to book reading statuses (e.g., reading, completed)")
@RestController
@RequestMapping(value = "/api/statuses", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class StatusController {

    private final IStatusService statusService;
    private final StatusWebMapper statusWebMapper;

    @Operation(summary = "Get all statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statuses fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> getAllStatuses() {
        List<Status> statuses = statusService.getAll();
        List<StatusResponseDTO> responseList = statuses.stream()
                .map(statusWebMapper::toResponseDto)
                .peek(dto -> {
                    dto.add(linkTo(methodOn(StatusController.class).getStatusById(dto.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(StatusController.class).updateStatus(dto.getId(), null)).withRel("update"));
                    dto.add(linkTo(methodOn(StatusController.class).deleteStatus(dto.getId())).withRel("delete"));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
    @Operation(summary = "Get a status by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> getStatusById(@PathVariable Long id) {
        Status status = statusService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status with id " + id + " not found"));

        StatusResponseDTO dto = statusWebMapper.toResponseDto(status);
        dto.add(linkTo(methodOn(StatusController.class).getStatusById(id)).withSelfRel());
        dto.add(linkTo(methodOn(StatusController.class).updateStatus(id, null)).withRel("update"));
        dto.add(linkTo(methodOn(StatusController.class).deleteStatus(id)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Create a new status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Status created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PostMapping
    public ResponseEntity<StatusResponseDTO> createStatus(@Valid @RequestBody StatusRequestDTO requestDTO) {
        Status created = statusService.create(statusWebMapper.toDomain(requestDTO));
        StatusResponseDTO dto = statusWebMapper.toResponseDto(created);

        dto.add(linkTo(methodOn(StatusController.class).getStatusById(created.getId())).withSelfRel());
        dto.add(linkTo(methodOn(StatusController.class).updateStatus(created.getId(), null)).withRel("update"));
        dto.add(linkTo(methodOn(StatusController.class).deleteStatus(created.getId())).withRel("delete"));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @Operation(summary = "Update an existing status by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Status not found"),
            @ApiResponse(responseCode = "400", description = "Invalid update data")
    })
    @PreAuthorize("#requestDTO.userId == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> updateStatus(@PathVariable Long id,
                                                          @Valid @RequestBody StatusRequestDTO requestDTO) {
        Status updated = statusWebMapper.toDomain(requestDTO);
        updated.setId(id); // burada id'yi elle set etmek zorundayız çünkü DTO'dan gelmiyor
        Status result = statusService.update(id, updated);
        StatusResponseDTO dto = statusWebMapper.toResponseDto(result);

        dto.add(linkTo(methodOn(StatusController.class).getStatusById(id)).withSelfRel());
        dto.add(linkTo(methodOn(StatusController.class).deleteStatus(id)).withRel("delete"));

        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Delete a status by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    // TODO : EKSTRA CONFIG
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        statusService.getById(id).ifPresentOrElse(
                value -> statusService.delete(id),
                () -> {
                    throw new EntityNotFoundException("Status with id " + id + " not found");
                }
        );
        return ResponseEntity.noContent().build();
    }
}
