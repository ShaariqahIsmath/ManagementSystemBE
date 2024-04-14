package com.managementsystem.studentmanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.managementsystem.studentmanagement.domains.Course;
import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.dto.ModuleDto;
import com.managementsystem.studentmanagement.repositories.AssessmentRepository;
import com.managementsystem.studentmanagement.repositories.CourseRepository;
import com.managementsystem.studentmanagement.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private final ModuleRepository moduleRepository;

    @Autowired
    private final AssessmentRepository assessmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public ModuleService(CourseRepository courseRepository, ModuleRepository moduleRepository, AssessmentRepository assessmentRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public ResponseEntity<String> addModule(ModuleDto moduleDto) {
        try {
            // Retrieve the course by ID
            Course course = courseRepository.findById(moduleDto.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + moduleDto.getCourse().getId()));

            // Create a new module entity
            Module module = new Module();
            module.setCode(moduleDto.getCode());
            module.setName(moduleDto.getName());
            module.setDuration(moduleDto.getDuration());
            module.setCourse(course);

            // Save the module to the database
            moduleRepository.save(module);

            return ResponseEntity.status(HttpStatus.OK).body("Module added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add module!");
        }
    }


}
