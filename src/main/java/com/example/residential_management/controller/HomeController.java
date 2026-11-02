package com.example.residential_management.controller;

import com.example.residential_management.entity.Student;
import com.example.residential_management.mapper.StudentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private StudentMapper studentMapper;

    // ==========================================
    // TRANG CHỦ
    // ==========================================

    @GetMapping({"/", "/home"})
    public String showHome(Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        "ADMIN".equals(authority.getAuthority()));

        if (!isAdmin) {
            return "redirect:/login";
        }

        return "redirect:/admin/dashboard";
    }

    // ==========================================
    // ADMIN DASHBOARD
    // ==========================================

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        "ADMIN".equals(authority.getAuthority()));

        if (!isAdmin) {
            return "redirect:/login";
        }

        // Tên tài khoản
        model.addAttribute("fullName", authentication.getName());

        // Danh sách 5 học sinh mới nhất
        List<Student> students =
                studentMapper.findRecentStudents();

        model.addAttribute("students", students);

        return "admin/dashboard";
    }
}