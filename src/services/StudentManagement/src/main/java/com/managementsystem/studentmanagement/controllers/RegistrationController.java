package com.managementsystem.studentmanagement.controllers;

import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.domains.*;
import com.managementsystem.studentmanagement.dto.ModuleDto;
import com.managementsystem.studentmanagement.dto.RegistrationDto;
import com.managementsystem.studentmanagement.repositories.*;
import com.managementsystem.studentmanagement.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/registration")
public class RegistrationController {


    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register-student/{studentId}/enrol")
    public ResponseEntity<String> registerStudentForCourse(@PathVariable Long studentId, @RequestBody RegistrationDto registrationDto) {
        return registrationService.registerStudentForCourse(studentId, registrationDto);
    }

    @PutMapping("/update/{registrationId}")
    public ResponseEntity<String> updateRegistrationDetails(
            @PathVariable UUID registrationId,
            @RequestBody RegistrationDto updatedRegistrationDto
    ) {
        try {
            Registration updatedRegistration = registrationService.updateRegistrationDetails(registrationId, updatedRegistrationDto);
            return ResponseEntity.ok("Registration details updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update registration: " + e.getMessage());
        }
    }




}
