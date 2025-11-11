package com.example.spring.mappers;

import com.example.spring.dtos.auth.JwtDataDto;
import com.example.spring.dtos.auth.RegisterRequestDto;
import com.example.spring.dtos.auth.RegisterResponseDto;
import com.example.spring.dtos.user.UserSearchResponseDto;
import com.example.spring.dtos.user.UserUpdateRequestDto;
import com.example.spring.enums.Role;
import com.example.spring.models.User;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", imports = {Role.class})
public interface UserMapper {
    @Mapping(target="password", source = "encodedPassword")
    @Mapping(target="username", expression = "java(requestDto.username())")
    @Mapping(target="role", expression = "java(Role.USER)")
    User toUser(RegisterRequestDto requestDto, String encodedPassword);

    @Mapping(target = "id", source = "id")
    JwtDataDto toJwtDataDto(User user);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    RegisterResponseDto toDto(User user);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserSearchResponseDto toUserSearchResponseDto(User user);

    default Page<UserSearchResponseDto> toUserSearchResponseDto(Page<User> users) {
        return users.map(this::toUserSearchResponseDto);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", source = "dto.role")
    @Mapping(target = "username", source = "dto.username")
    void updateUserFromDto(UserUpdateRequestDto dto, @MappingTarget User user);
}
