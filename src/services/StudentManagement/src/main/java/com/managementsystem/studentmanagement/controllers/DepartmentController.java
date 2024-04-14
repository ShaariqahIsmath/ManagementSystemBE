package com.managementsystem.studentmanagement.controllers;

import com.managementsystem.studentmanagement.domains.Department;
import com.managementsystem.studentmanagement.dto.DepartmentDto;
import com.managementsystem.studentmanagement.enums.DepartmentNames;
import com.managementsystem.studentmanagement.repositories.DepartmentRepository;
import com.managementsystem.studentmanagement.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDepartment(@RequestBody DepartmentDto departmentDto) {
        return departmentService.addDepartment(departmentDto);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllDepartments() {
        List<Map<String, Object>> courses = departmentService.getAllDepartments();
        return ResponseEntity.ok(courses);

    }
}
