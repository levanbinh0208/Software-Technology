
package com.example.residential_management.service;

import com.example.residential_management.entity.Student;
import com.example.residential_management.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public List<Student> findAll(String keyword, Integer classId, String boardingStatus) {
        return studentMapper.findAll(keyword, classId, boardingStatus);
    }

    public Student findById(Long studentId) {
        return studentMapper.findById(studentId);
    }

    @Transactional
    public void create(Student student) {
        validateStudent(student);
        int exists = studentMapper.existsByStudentCode(student.getStudentCode(), null);
        if (exists > 0) {
            throw new IllegalArgumentException("Mã học sinh đã tồn tại!");
        }
        studentMapper.insert(student);
    }

    @Transactional
    public void update(Student student) {
        if (student.getStudentId() == null || studentMapper.findById(student.getStudentId()) == null) {
            throw new IllegalArgumentException("Không tìm thấy học sinh!");
        }

        validateStudent(student);

        int exists = studentMapper.existsByStudentCode(student.getStudentCode(), student.getStudentId());

        if (exists > 0) {
            throw new IllegalArgumentException("Mã học sinh đã tồn tại!");
        }

        studentMapper.update(student);
    }

    @Transactional
    public void delete(Long studentId) {

        if (studentMapper.findById(studentId) == null) {
            throw new IllegalArgumentException("Không tìm thấy học sinh!");
        }

        studentMapper.deleteById(studentId);
    }

    private void validateStudent(Student student) {
        if (student.getStudentCode() == null || student.getStudentCode().isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập mã học sinh!");
        }

        if (student.getFullName() == null || student.getFullName().isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập họ tên học sinh!");
        }

        if (student.getClassId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn lớp!");
        }

        if (student.getBoardingStatus() == null || !(student.getBoardingStatus().equals("ACTIVE") || student.getBoardingStatus().equals("LEFT"))) {
            throw new IllegalArgumentException("Trạng thái nội trú không hợp lệ!");
        }

        if (student.getParentPhone() != null && !student.getParentPhone().isBlank() && !student.getParentPhone().matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Số điện thoại phụ huynh phải có 10-11 chữ số!");
        }
    }
}
