package com.example.residential_management.controller;

import com.example.residential_management.entity.User;
import com.example.residential_management.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            if ("ADMIN".equals(user.getRoleName())) {
                return "redirect:/admin/dashboard";
            }
        }

        return "login";
    }


    @PostMapping("/login")
    public String handleLogin(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            HttpSession session,
            Model model) {

        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập tên đăng nhập.");
            return "login";
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập mật khẩu.");
            return "login";
        }

        User user = userService.login(username.trim(), password);
        if (user == null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return "login";
        }

        if (!"ADMIN".equals(user.getRoleName())) {
            model.addAttribute("error", "Tài khoản không có quyền truy cập hệ thống quản lý nội trú.");
            return "login";
        }

        session.setAttribute("loggedInUser", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("roleName", user.getRoleName());
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {session.invalidate();
        return "redirect:/login";
    }
}