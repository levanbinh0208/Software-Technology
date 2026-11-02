
package com.example.residential_management.mapper;

import com.example.residential_management.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User findByUsername(@Param("username") String username);

    User findById(@Param("id") Long id);

    int insert(User user);

    List<User> findAll();

    int updateRole(
            @Param("userId") Long userId,
            @Param("roleName") String roleName);

    int deleteById(@Param("userId") Long userId);

    User login(
            @Param("username") String username,
            @Param("password") String password);

    User findByEmail(@Param("email") String email);

    int updatePassword(
            @Param("userId") Long userId,
            @Param("password") String password);
}
