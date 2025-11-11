package com.example.spring.services;

import com.example.spring.dtos.auth.LoginRequestDto;
import com.example.spring.dtos.auth.LoginResponseDto;
import com.example.spring.dtos.auth.RegisterRequestDto;
import com.example.spring.dtos.auth.RegisterResponseDto;
import com.example.spring.models.User;

import java.util.Optional;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto requestDto);
    LoginResponseDto login(LoginRequestDto requestDto);
    void logout(String token);
    Optional<User> getAuthenticatedUser();
}
