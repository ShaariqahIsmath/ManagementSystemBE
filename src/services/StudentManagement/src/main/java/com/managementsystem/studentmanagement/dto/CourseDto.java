package com.managementsystem.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CourseDto {

    private Long id;
    private String name;
    private String type;
    private DepartmentDto department;
    private List<ModuleDto> modules;

    public CourseDto() {
    }

    public CourseDto(String name, String type, DepartmentDto departmentDto) {
        this.name = name;
        this.type = type;
        this.department = departmentDto;
    }

    public CourseDto(String name, String type, DepartmentDto departmentDto, List<ModuleDto> modules) {
        this.name = name;
        this.type = type;
        this.department = departmentDto;
        this.modules = modules;
    }

}
