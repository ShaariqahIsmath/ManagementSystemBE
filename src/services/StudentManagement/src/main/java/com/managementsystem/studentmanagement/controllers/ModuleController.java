package com.managementsystem.studentmanagement.controllers;

import com.managementsystem.studentmanagement.domains.Course;
import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.dto.ModuleDto;
import com.managementsystem.studentmanagement.repositories.CourseRepository;
import com.managementsystem.studentmanagement.repositories.ModuleRepository;
import com.managementsystem.studentmanagement.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private final ModuleRepository moduleRepository;

    @Autowired
    private final ModuleService moduleService;

    @Autowired
    private final CourseRepository courseRepository;


    @Autowired
    public ModuleController(ModuleRepository moduleRepository, ModuleService moduleService, CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.moduleService = moduleService;
        this.courseRepository = courseRepository;
    }


    @PostMapping("/add")
    public ResponseEntity<String> addModule(@RequestBody ModuleDto moduleDto) {
        return moduleService.addModule(moduleDto);
    }
}
