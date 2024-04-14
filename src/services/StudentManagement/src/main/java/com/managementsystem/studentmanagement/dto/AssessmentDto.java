package com.managementsystem.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AssessmentDto {

    private Long id;
    private String moduleCode;
    private String name; // Assuming AssessmentTypes is an enum for assessment type
    private List<AssessmentDetailsDto> assessmentDetailsList;

    public AssessmentDto() {
    }

    public AssessmentDto(Long id, String moduleCode, String name, List<AssessmentDetailsDto> assessmentDetailsList) {
        this.id = id;
        this.moduleCode = moduleCode;
        this.name = name;
        this.assessmentDetailsList = assessmentDetailsList;
    }
}
