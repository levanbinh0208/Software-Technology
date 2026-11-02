
package com.example.residential_management.mapper;

import com.example.residential_management.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    // Danh sách và tìm kiếm học sinh
    List<Student> findAll(
            @Param("keyword") String keyword,
            @Param("classId") Integer classId,
            @Param("boardingStatus") String boardingStatus
    );

    // Tìm học sinh theo ID
    Student findById(@Param("studentId") Long studentId);

    // Kiểm tra mã học sinh đã tồn tại
    int existsByStudentCode(
            @Param("studentCode") String studentCode,
            @Param("studentId") Long studentId
    );

    // Thêm học sinh
    int insert(Student student);

    // Cập nhật học sinh
    int update(Student student);

    // Xóa học sinh
    int deleteById(@Param("studentId") Long studentId);

    List<Student> findRecentStudents();

    int countAll();

    int countByBoardingStatus(@Param("status") String status);
}
