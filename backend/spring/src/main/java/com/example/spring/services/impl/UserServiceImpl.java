package com.example.spring.services.impl;

import com.example.spring.dtos.user.UserSearchRequestDto;
import com.example.spring.dtos.user.UserSearchResponseDto;
import com.example.spring.dtos.user.UserUpdateRequestDto;
import com.example.spring.enums.Role;
import com.example.spring.exceptions.UnauthorizedException;
import com.example.spring.exceptions.UserNotFoundException;
import com.example.spring.mappers.UserMapper;
import com.example.spring.models.User;
import com.example.spring.repositories.UserRepository;
import com.example.spring.services.AuthService;
import com.example.spring.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    @Override
    public Page<UserSearchResponseDto> searchUsers(UserSearchRequestDto params, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(params.username(), params.role(), pageable);
        return userMapper.toUserSearchResponseDto(users);
    }

    @Override
    public UserSearchResponseDto findById(UUID id) {
        User userAuth = authService.getAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado"));

        if (!userAuth.getRole().equals(Role.ADMIN) && !userAuth.getId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para ver este perfil");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        return userMapper.toUserSearchResponseDto(user);
    }

    @Override
    public UserSearchResponseDto updateById(UUID id, @Valid UserUpdateRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Usuario no encontrado"));

        userMapper.updateUserFromDto(request,user);

        return userMapper.toUserSearchResponseDto(userRepository.save(user));
    }

    @Override
    public void deleteById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }
}
