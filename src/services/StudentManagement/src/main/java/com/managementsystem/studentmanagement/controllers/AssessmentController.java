package com.managementsystem.studentmanagement.controllers;

import com.managementsystem.studentmanagement.domains.Assessment;
import com.managementsystem.studentmanagement.domains.AssessmentDetails;
import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.dto.AssessmentDetailsDto;
import com.managementsystem.studentmanagement.dto.AssessmentDto;
import com.managementsystem.studentmanagement.enums.AssessmentTypes;
import com.managementsystem.studentmanagement.repositories.AssessmentDetailsRepository;
import com.managementsystem.studentmanagement.repositories.AssessmentRepository;
import com.managementsystem.studentmanagement.repositories.ModuleRepository;
import com.managementsystem.studentmanagement.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentDetailsRepository assessmentDetailsRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private AssessmentService assessmentService;


    @PostMapping("/add")
    public ResponseEntity<String> addAssessment(@RequestBody AssessmentDto assessmentDto) {
        try {
            Assessment assessment = assessmentService.createAssessment(assessmentDto);
            assessmentRepository.save(assessment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Assessment added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add assessment: " + e.getMessage());
        }
    }

    @PutMapping("/{studentId}/update/{assessmentId}")
    public ResponseEntity<String> updateAssessmentDetails(
            @PathVariable Long studentId,
            @PathVariable Long assessmentId,
            @RequestBody AssessmentDetailsDto updatedAssessmentDetailsDto) {

        return assessmentService.updateAssessmentDetails(studentId, assessmentId, updatedAssessmentDetailsDto);
    }

    @GetMapping("/{studentId}/{assessmentId}")
    public ResponseEntity<AssessmentDetailsDto> getAssessmentDetailsByStudentAndAssessment(
            @PathVariable Long studentId, @PathVariable Long assessmentId) throws ChangeSetPersister.NotFoundException {
        AssessmentDetailsDto assessmentDetailsDTO = assessmentService.getAssessmentDetailsByStudentAndAssessment(studentId, assessmentId);
        return ResponseEntity.ok(assessmentDetailsDTO);
    }









}
