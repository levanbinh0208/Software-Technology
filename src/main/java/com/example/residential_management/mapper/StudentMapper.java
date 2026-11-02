
package com.example.residential_management.mapper;

import com.example.residential_management.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<Student> findAll(@Param("keyword") String keyword, @Param("classId") Integer classId, @Param("boardingStatus") String boardingStatus);

    Student findById(@Param("studentId") Long studentId);

    int existsByStudentCode(@Param("studentCode") String studentCode, @Param("studentId") Long studentId);

    int insert(Student student);

    int update(Student student);

    int deleteById(@Param("studentId") Long studentId);

    List<Student> findRecentStudents();

}
