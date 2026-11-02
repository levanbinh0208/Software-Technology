package com.example.residential_management.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Vui lòng nhập tên đăng nhập")
        String username,

        @NotBlank(message = "Vui lòng nhập mật khẩu")
        String password
) {
}