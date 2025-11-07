package com.example.spring.services.impl;

import com.example.spring.dtos.auth.LoginRequestDto;
import com.example.spring.dtos.auth.LoginResponseDto;
import com.example.spring.dtos.auth.RegisterRequestDto;
import com.example.spring.dtos.auth.RegisterResponseDto;
import com.example.spring.mappers.UserMapper;
import com.example.spring.models.User;
import com.example.spring.repositories.UserRepository;
import com.example.spring.security.CustomUserDetails;
import com.example.spring.security.service.JwtService;
import com.example.spring.security.service.TokenBlacklistService;
import com.example.spring.services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    // --- Repositories ---
    private final UserRepository userRepository;

    // --- Security & Auth ---
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    // --- Mappers ---
    private final UserMapper userMapper;

    /**
     * Register a new user.
     */
    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        User user = userMapper.toUser(requestDto, encodedPassword);

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    /**
     * Performs authentication and generates JWT for the user.
     */
    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.username(), requestDto.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        String token = jwtService.generateToken(userMapper.toJwtDataDto(user));
        return new LoginResponseDto(token);
    }

    /**
     * Add the token to the blacklist to log out.
     */
    @Override
    public void logout(String token) {
        LocalDateTime expiration = jwtService.extractExpiration(token);
        tokenBlacklistService.addTokenToBlacklist(token, expiration);
    }

    /**
     * Gets the currently authenticated user.
     */
    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

}
