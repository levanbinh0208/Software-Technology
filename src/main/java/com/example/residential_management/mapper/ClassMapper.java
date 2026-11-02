
package com.example.residential_management.mapper;

import com.example.residential_management.entity.Classes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassMapper {

    List<Classes> findAll();
}
