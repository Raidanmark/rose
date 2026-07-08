package com.rose.user.dto;

public record UserDto(
        Long id,
        String username,
        String email
) {
}