package com.example.residential_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class JwtTestController {

    @GetMapping("/test")
    public String test() {
        return "JWT Authentication thành công! Bạn có quyền ADMIN.";
    }
}