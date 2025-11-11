package com.example.spring.controllers;

import com.example.spring.documentation.auth.LoginEndpointDoc;
import com.example.spring.documentation.auth.LogoutEndpointDoc;
import com.example.spring.documentation.auth.RegisterEndpointDoc;
import com.example.spring.dtos.auth.LoginRequestDto;
import com.example.spring.dtos.auth.LoginResponseDto;
import com.example.spring.dtos.auth.RegisterRequestDto;
import com.example.spring.dtos.auth.RegisterResponseDto;
import com.example.spring.exceptions.UnauthorizedException;
import com.example.spring.services.AuthService;
import com.example.spring.utils.ApiResult;
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

    @RegisterEndpointDoc
    @PostMapping("/register")
    public ResponseEntity<ApiResult<?>> register(@RequestBody @Valid RegisterRequestDto requestDto) {
        RegisterResponseDto response = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(response, "Usuario registrado exitosamente."));
    }

    @LoginEndpointDoc
    @PostMapping("/login")
    public ResponseEntity<ApiResult<?>> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(ApiResult.success(response, "Login exitoso."));
    }

    @LogoutEndpointDoc
    @PostMapping("/logout")
    public ResponseEntity<ApiResult<?>> logout(HttpServletRequest requestDto) {
        String authHeader = requestDto.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Acceso no autorizado. Token ausente.");
        }
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok(ApiResult.success("Logout exitoso."));
    }
}
