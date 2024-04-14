package com.managementsystem.studentmanagement.dto;

import com.managementsystem.studentmanagement.domains.Student;
import com.managementsystem.studentmanagement.enums.AssessmentTypes;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
public class AssessmentDetailsDto {

    private Long id;
    private StudentDto studentId;
    private RegistrationDto registrationId;
    private ModuleDto moduleCode;
    private AssessmentTypes name;
    private int marks;


    public AssessmentDetailsDto() {
    }

    public AssessmentDetailsDto(Long id, StudentDto studentId, RegistrationDto registrationId, ModuleDto moduleCode, AssessmentTypes name, int marks) {
        this.id = id;
        this.studentId = studentId;
        this.registrationId = registrationId;
        this.moduleCode = moduleCode;
        this.name = name;
        this.marks = marks;
    }
}
