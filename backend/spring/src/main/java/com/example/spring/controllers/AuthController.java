package com.example.spring.controllers;

import com.example.spring.dtos.auth.LoginRequestDto;
import com.example.spring.dtos.auth.LoginResponseDto;
import com.example.spring.dtos.auth.RegisterRequestDto;
import com.example.spring.dtos.auth.RegisterResponseDto;
import com.example.spring.services.AuthService;
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
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto requestDto) {
        RegisterResponseDto response = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest requestDto) {
        String authHeader = requestDto.getHeader("Authorization");
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }
}
