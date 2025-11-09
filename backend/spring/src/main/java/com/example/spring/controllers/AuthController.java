package com.example.spring.controllers;

import com.example.spring.dtos.auth.LoginRequestDto;
import com.example.spring.dtos.auth.LoginResponseDto;
import com.example.spring.dtos.auth.RegisterRequestDto;
import com.example.spring.dtos.auth.RegisterResponseDto;
import com.example.spring.exceptions.dto.ErrorResponse;
import com.example.spring.services.AuthService;
import com.example.spring.utils.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "01 - Autenticación",
        description = "Endpoints para autenticación de usuarios y gestión de cuentas")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Usuario registrado exitosamente.",
                  "data": {
                    "username": "john_doe",
                    "role": "USER"
                  }
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación - Datos incorrectos o incompletos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error de validación en los siguientes campos: username, password",
                  "error": "VALIDATION_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/register",
                  "details": {
                    "username": "el nombre de usuario debe tener entre 3 y 20 caracteres",
                    "password": "la contraseña es obligatoria"
                  }
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto - Usuario ya existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Usuario ya existe con username: john_doe",
                  "error": "DUPLICATE_RESOURCE",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/register"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error interno del servidor. Contacta al administrador.",
                  "error": "INTERNAL_SERVER_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/register"
                }
            """)
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResult<?>> register(@RequestBody @Valid RegisterRequestDto requestDto) {
        RegisterResponseDto response = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(response, "Usuario registrado exitosamente."));
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y devuelve un token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "success": true,
                  "message": "Login exitoso.",
                  "data": {
                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                  }
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación - Datos incorrectos o incompletos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error de validación en los siguientes campos: username",
                  "error": "VALIDATION_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/login",
                  "details": {
                    "username": "el nombre de usuario es obligatorio"
                  }
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales incorrectas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Credenciales incorrectas. Verifica tu usuario y contraseña.",
                  "error": "AUTHENTICATION_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/login"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "success": false,
                  "message": "Error interno del servidor.",
                  "error": "INTERNAL_SERVER_ERROR",
                  "timestamp": "2025-11-09T14:30:00",
                  "path": "/api/auth/login"
                }
            """)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResult<?>> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(ApiResult.success(response, "Login exitoso."));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResult<?>> logout(HttpServletRequest requestDto) {
        String authHeader = requestDto.getHeader("Authorization");
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok(ApiResult.success("Logout exitoso."));
    }
}
