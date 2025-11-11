package com.example.spring.services;

import com.example.spring.dtos.user.UserSearchRequestDto;
import com.example.spring.dtos.user.UserSearchResponseDto;
import com.example.spring.dtos.user.UserUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    Page<UserSearchResponseDto> searchUsers(UserSearchRequestDto params, Pageable pageable);
    UserSearchResponseDto findById(UUID id);
    UserSearchResponseDto updateById(UUID id, UserUpdateRequestDto request);
    void deleteById(UUID id);
}
