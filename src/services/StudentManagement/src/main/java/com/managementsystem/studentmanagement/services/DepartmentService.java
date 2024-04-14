package com.managementsystem.studentmanagement.services;

import com.managementsystem.studentmanagement.domains.Course;
import com.managementsystem.studentmanagement.domains.Department;
import com.managementsystem.studentmanagement.dto.DepartmentDto;
import com.managementsystem.studentmanagement.enums.DepartmentNames;
import com.managementsystem.studentmanagement.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Map<String, Object>> getAllDepartments() {
        List<Department> courses = departmentRepository.findAll();

        return courses.stream()
                .map(course -> {
                    Map<String, Object> courseMap = new HashMap<>();
                    courseMap.put("id", course.getId());
                    courseMap.put("name", course.getName());
                    return courseMap;
                })
                .collect(Collectors.toList());
    }


        public ResponseEntity<String> addDepartment(DepartmentDto departmentDto) {
            try {
                String departmentCategoryName = departmentDto.getName();
                DepartmentNames departmentName;

                try {
                    departmentName = DepartmentNames.valueOf(departmentCategoryName.replace(" ", "_").toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid department name: " + departmentCategoryName);
                }

                // Create a new department entity
                Department department = new Department();
                department.setName(departmentName);
                department.setInCharge(departmentDto.getInCharge());

                // You might want to set students and courses here,
                // based on the IDs provided in the DTO.
                // For simplicity, I'll just set the inCharge and name for now.

                // Save the department to the database
                departmentRepository.save(department);

                return ResponseEntity.status(HttpStatus.CREATED).body("Department added successfully!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add department: " + e.getMessage());
            }
        }


}
