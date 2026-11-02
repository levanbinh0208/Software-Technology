package com.example.residential_management.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        Long userId,
        String username,
        String fullName,
        String role
) {
}
