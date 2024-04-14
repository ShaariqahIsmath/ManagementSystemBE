package com.managementsystem.studentmanagement.controllers;

import com.managementsystem.studentmanagement.dto.CourseDto;
import com.managementsystem.studentmanagement.repositories.CourseRepository;
import com.managementsystem.studentmanagement.repositories.DepartmentRepository;
import com.managementsystem.studentmanagement.repositories.ModuleRepository;
import com.managementsystem.studentmanagement.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    private final ModuleRepository moduleRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    public CourseController(CourseRepository courseRepository, DepartmentRepository departmentRepository, ModuleRepository moduleRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.moduleRepository = moduleRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody CourseDto courseDto) {
        try {
            CourseDto savedCourse = courseService.addCourse(courseDto);
            return ResponseEntity.ok("Course added successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the course.");
        }
    }


    // Get all courses
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllCourses() {
        List<Map<String, Object>> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
}


