
package com.example.residential_management.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {

    private Long userId;

    private String fullName;

    private String userName;

    private String email;

    private String passwordHash;

    private String phone;

    private String address;

    private Integer roleId;

    private String roleName;

    private Boolean isActive;

    private LocalDateTime createdAt;
}
