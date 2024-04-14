package com.managementsystem.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.managementsystem.studentmanagement.domains.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ModuleDto {

    private String code;
    private String name;
    private String duration;
    private Course course;
    private List<AssessmentDto> assessments;  // Added this line


    public ModuleDto() {
    }

    public ModuleDto(String code, String name, String duration, Course courseDto) {
        this.code = code;
        this.name = name;
        this.duration = duration;
        this.course = courseDto;

    }

    public ModuleDto(String moduleCode) {
        this.code = moduleCode;
    }
}
