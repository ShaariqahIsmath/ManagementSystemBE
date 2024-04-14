package com.managementsystem.studentmanagement.services;

import com.managementsystem.studentmanagement.domains.Assessment;
import com.managementsystem.studentmanagement.domains.AssessmentDetails;
import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.dto.*;
import com.managementsystem.studentmanagement.enums.AssessmentTypes;
import com.managementsystem.studentmanagement.repositories.AssessmentDetailsRepository;
import com.managementsystem.studentmanagement.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssessmentService {


    @Autowired
    private AssessmentDetailsRepository assessmentDetailsRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public AssessmentDetailsDto getAssessmentDetailsByStudentAndAssessment(Long studentId, Long assessmentId) throws ChangeSetPersister.NotFoundException {
        // Find the AssessmentDetails entity by studentId and assessmentId
        AssessmentDetails assessmentDetails = assessmentDetailsRepository.findByStudentIdAndAssessmentId(studentId, assessmentId)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());

        AssessmentDetailsDto assessmentDetailsDTO = new AssessmentDetailsDto();
        assessmentDetailsDTO.setId(assessmentDetails.getId());
        assessmentDetailsDTO.setStudentId(new StudentDto(assessmentDetails.getStudent().getId())); // Assuming StudentDto has a constructor that accepts an ID
        assessmentDetailsDTO.setRegistrationId(new RegistrationDto(assessmentDetails.getRegistration().getId())); // Assuming RegistrationDto has a constructor that accepts an ID
        assessmentDetailsDTO.setMarks(assessmentDetails.getMarks());

        // Iterate through the assessmentDetailsList of the student to find the moduleCode and name
        String moduleCode = null;
        String assessmentName = null;
        for (AssessmentDetails studentAssessmentDetails : assessmentDetails.getStudent().getAssessmentDetailsList()) {
            if (studentAssessmentDetails.getAssessment().getId().equals(assessmentId)) {
                moduleCode = studentAssessmentDetails.getAssessment().getModule().getCode();
                assessmentName = String.valueOf(studentAssessmentDetails.getAssessment().getName());
                break;
            }
        }

        if (moduleCode == null || assessmentName == null) {
            throw new ChangeSetPersister.NotFoundException();
        }

        assessmentDetailsDTO.setModuleCode(new ModuleDto(moduleCode)); // Assuming ModuleDto has a constructor that accepts a code
        assessmentDetailsDTO.setName(AssessmentTypes.valueOf(assessmentName)); // Assuming AssessmentTypes is an enum

        return assessmentDetailsDTO;
    }

    public Assessment createAssessment(AssessmentDto assessmentDto) {
        Module module = moduleRepository.findByCode(assessmentDto.getModuleCode());
        if (module == null) {
            throw new RuntimeException("Module not found with code: " + assessmentDto.getModuleCode());
        }

        String assessmentTypeName = assessmentDto.getName();
        String cleanedTypeName = cleanStringForEnum(assessmentTypeName);

        AssessmentTypes assessmentType = findMatchingEnum(cleanedTypeName);
        if (assessmentType == null) {
            throw new RuntimeException("Invalid assessment type: " + assessmentTypeName);
        }

        Assessment assessment = new Assessment();
        assessment.setModule(module);
        assessment.setName(assessmentType);

        return assessment;
    }

    private String cleanStringForEnum(String value) {
        return value.toUpperCase().replace(" ", "_").replace("-", "_");
    }

    private AssessmentTypes findMatchingEnum(String cleanedTypeName) {
        for (AssessmentTypes type : AssessmentTypes.values()) {
            String cleanedEnumName = cleanStringForEnum(type.name());
            if (cleanedEnumName.equals(cleanedTypeName)) {
                return type;
            }
        }
        return null;
    }

    public ResponseEntity<String> updateAssessmentDetails(Long studentId, Long assessmentId, AssessmentDetailsDto updatedAssessmentDetailsDto) {
        try {
            // Fetch the existing assessment details based on studentId and assessmentId using the repository
            Optional<AssessmentDetails> existingAssessmentDetailsOpt = assessmentDetailsRepository.findByStudentIdAndAssessmentId(studentId, assessmentId);

            if (existingAssessmentDetailsOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            AssessmentDetails existingAssessmentDetails = existingAssessmentDetailsOpt.get();

            // Update the marks in the existing assessment details
            existingAssessmentDetails.setMarks(updatedAssessmentDetailsDto.getMarks());

            // Save/update the updated assessment details
            assessmentDetailsRepository.save(existingAssessmentDetails);

            return ResponseEntity.ok("Assessment details updated successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update assessment details: " + e.getMessage());
        }
    }





}
