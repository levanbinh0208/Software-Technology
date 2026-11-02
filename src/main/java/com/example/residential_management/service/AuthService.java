package com.example.residential_management.service;

import com.example.residential_management.dto.LoginRequest;
import com.example.residential_management.dto.LoginResponse;
import com.example.residential_management.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(
            UserService userService,
            JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        // Kiểm tra tài khoản và mật khẩu bằng logic hiện tại
        User user = userService.login(
                request.username().trim(),
                request.password()
        );

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Tên đăng nhập hoặc mật khẩu không đúng"
            );
        }

        // Tạo JWT
        String token = jwtService.generateToken(user);

        // Trả thông tin người dùng và Token
        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs() / 1000,
                user.getUserId(),
                user.getUserName(),
                user.getFullName(),
                user.getRoleName()
        );
    }
}