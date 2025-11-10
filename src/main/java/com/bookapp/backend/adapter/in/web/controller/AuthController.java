package com.bookapp.backend.adapter.in.web.controller;

import com.bookapp.backend.adapter.in.web.dto.request.user.UserLoginRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.user.UserRegisterRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserLoginResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserRegisterResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.UserWebLoginMapper;
import com.bookapp.backend.adapter.in.web.mapper.UserWebRegisterMapper;
import com.bookapp.backend.application.service.AuthUseCases;
import com.bookapp.backend.application.service.JwtUseCases;
import com.bookapp.backend.domain.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Authentication", description = "Login/Register/Logout")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUseCases jwtService;
    private final UserWebLoginMapper userWebLoginMapper;
    private final UserWebRegisterMapper userWebRegisterMapper;
    private final AuthUseCases authService;

    @Operation(summary = "User Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "Username or password is incorrect."),
            @ApiResponse(responseCode = "400", description = "Incorrect request data")
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO dto) {
        User user = userWebLoginMapper.toDomain(dto);
        User authenticatedUser = authService.login(user);

        String jwt = jwtService.generateToken(authenticatedUser);

        UserLoginResponseDTO response = new UserLoginResponseDTO();
        response.setId(authenticatedUser.getId());
        response.setUsername(authenticatedUser.getUsername());
        response.setToken(jwt);

        response.add(linkTo(methodOn(AuthController.class).login(null)).withSelfRel());
        response.add(linkTo(methodOn(AuthController.class).logout()).withRel("logout"));
        response.add(linkTo(methodOn(UserController.class).getUserById(authenticatedUser.getId())).withRel("profile"));

        return ResponseEntity
                .ok()
                .body(response);
    }
    @Operation(summary = "New user can register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful"),
            @ApiResponse(responseCode = "409", description = "Email or username are already taken"),
            @ApiResponse(responseCode = "400", description = "Incorrect request data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO dto) {
        User user = userWebRegisterMapper.toDomain(dto);

        User savedUser = authService.register(user);

        UserRegisterResponseDTO responseDto = userWebRegisterMapper.toRegisterResponseDto(savedUser);

        responseDto.add(linkTo(methodOn(AuthController.class).register(null)).withSelfRel());
        responseDto.add(linkTo(methodOn(AuthController.class).login(null)).withRel("login"));

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUserById(savedUser.getId())).toUri())
                .body(responseDto);
    }
    @Operation(summary = "User log out (delete JWT TOKEN)")
    @ApiResponse(responseCode = "204", description = "Successful")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity
                .noContent()
                .build();
    }
}
