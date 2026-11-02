
package com.example.residential_management.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Student {

    private Long studentId;

    private String studentCode;

    private String fullName;

    private String gender;

    private LocalDate dateOfBirth;

    private Integer classId;

    private String className;

    private String hometown;

    private String parentName;

    private String parentPhone;

    // ACTIVE: Đang nội trú
    // LEFT: Đã ra nội trú
    private String boardingStatus;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
