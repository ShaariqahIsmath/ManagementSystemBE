package com.managementsystem.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DepartmentDto {

    private Long id;
    private String name;
    private String inCharge;
    private List<Long> studentIds; // Assuming you only need the IDs of students associated with the department
    private List<CourseDto> courses;

    public DepartmentDto() {
    }

    public DepartmentDto(Long id, String name, String inCharge) {
        this.id = id;
        this.name = name;
        this.inCharge = inCharge;
    }

    public DepartmentDto(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public DepartmentDto(Long id, String name, String inCharge, List<CourseDto> courses) {
        this.id = id;
        this.name = name;
        this.inCharge = inCharge;
        this.courses = courses;
    }
}
