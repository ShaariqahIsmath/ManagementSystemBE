package com.managementsystem.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegistrationDto {

    private UUID id;
    private LocalDate registrationDate;
    private String registrationMethod;
    private String registrationSource;
    private Long studentId;
    private CourseDto course;
    private DepartmentDto department;
    private List<ModuleDto> modules;

    public RegistrationDto() {
    }

    public RegistrationDto(UUID id) {
        this.id = id;
    }

    public RegistrationDto(UUID id, LocalDate registrationDate, String registrationMethod, String registrationSource) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.registrationMethod = registrationMethod;
        this.registrationSource = registrationSource;
    }

    public RegistrationDto(UUID id, LocalDate registrationDate, String registrationMethod, String registrationSource, Long studentId, CourseDto courseDto, DepartmentDto departmentDto) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.registrationMethod = registrationMethod;
        this.registrationSource = registrationSource;
        this.studentId = studentId;
        this.course = courseDto;
        this.department = departmentDto;
    }

    public RegistrationDto(List<ModuleDto> moduleDtoCodes) {
        this.modules = moduleDtoCodes;
    }
}
