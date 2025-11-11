package com.example.spring.controllers;

import com.example.spring.documentation.user.DeleteUserEndpointDoc;
import com.example.spring.documentation.user.GetAllUsersEndpointDoc;
import com.example.spring.documentation.user.GetUserByIdEndpointDoc;
import com.example.spring.documentation.user.UpdateUserEndpointDoc;
import com.example.spring.dtos.user.UserSearchRequestDto;
import com.example.spring.dtos.user.UserSearchResponseDto;
import com.example.spring.dtos.user.UserUpdateRequestDto;
import com.example.spring.services.UserService;
import com.example.spring.utils.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "02 - Usuarios",
        description = "Endpoints para la gestión de usuarios ")
public class UserController {

    private final UserService userService;

    @GetAllUsersEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllWithSearch(@ParameterObject @Valid UserSearchRequestDto params, @ParameterObject Pageable pageable) {
        Page<UserSearchResponseDto> response = userService.searchUsers(params, pageable);
        return ResponseEntity.ok()
                .body(ApiResult.success(response,"Operación exitosa"));
    }

    @GetUserByIdEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id){
        UserSearchResponseDto response = userService.findById(id);
        return ResponseEntity.ok().body(ApiResult.success(response,"Usuario encontrado"));
    }

    @UpdateUserEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody @Valid UserUpdateRequestDto request){
        UserSearchResponseDto response = userService.updateById(id, request);
        return ResponseEntity.ok().body(ApiResult.success(response,"Actualizacion eixosa"));
    }

    @DeleteUserEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
