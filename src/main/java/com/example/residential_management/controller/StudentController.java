
package com.example.residential_management.controller;

import com.example.residential_management.entity.Student;
import com.example.residential_management.mapper.ClassMapper;
import com.example.residential_management.service.StudentService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

    private final StudentService studentService;
    private final ClassMapper classMapper;

    public StudentController(StudentService studentService, ClassMapper classMapper) {
        this.studentService = studentService;
        this.classMapper = classMapper;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        "ADMIN".equals(authority.getAuthority()));
    }

    @GetMapping
    public String listStudents(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer classId,
                               @RequestParam(required = false) String boardingStatus, Authentication authentication, Model model) {
        if (!isAdmin(authentication)) {
            return "redirect:/login";
        }
        model.addAttribute("students", studentService.findAll(keyword, classId, boardingStatus));
        model.addAttribute("classes", classMapper.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("classId", classId);
        model.addAttribute("boardingStatus", boardingStatus);

        return "admin/list";
    }

    @GetMapping("/create")
    public String createForm(Authentication authentication, Model model) {

        if (!isAdmin(authentication)) {
            return "redirect:/login";
        }
        Student student = new Student();
        student.setBoardingStatus("ACTIVE");

        model.addAttribute("student", student);
        model.addAttribute("classes", classMapper.findAll());
        model.addAttribute("pageTitle", "Thêm học sinh");

        return "admin/form";
    }

    @PostMapping("/create")
    public String createStudent(@ModelAttribute Student student, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (!isAdmin(authentication)) {
            return "redirect:/login";
        }
        try {
            if (student.getBoardingStatus() == null || student.getBoardingStatus().isBlank()) {
                student.setBoardingStatus("ACTIVE");
            }
            studentService.create(student);

            redirectAttributes.addFlashAttribute("success", "Thêm học sinh thành công!");
            return "redirect:/admin/students";
        } catch (IllegalArgumentException e) {redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/students/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (!isAdmin(authentication)) {
            return "redirect:/login";
        }
        Student student = studentService.findById(id);
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy học sinh!");
            return "redirect:/admin/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("classes", classMapper.findAll());
        model.addAttribute("pageTitle", "Cập nhật học sinh");

        return "admin/form";
    }

    @PostMapping("/update")
    public String updateStudent(@ModelAttribute Student student, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (!isAdmin(authentication)) {
            return "redirect:/login";
        }

        try {
            studentService.update(student);
            redirectAttributes.addFlashAttribute("success", "Cập nhật học sinh thành công!");
            return "redirect:/admin/students";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/students/edit/"
                    + student.getStudentId();
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {

        if (!isAdmin(authentication)) {
            return "redirect:/login";
        }
        try {
            studentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Xóa học sinh thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/students";
    }
}
