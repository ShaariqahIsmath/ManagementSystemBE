package com.managementsystem.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private String emergencyContact;
    private String address;
    private DepartmentDto department;
    private CourseDto course;
    private List<AssessmentDto> assessment;
    private RegistrationDto registration;
    private List<ModuleDto> module;

    // Constructors

    // Default constructor
    public StudentDto() {
    }

    public StudentDto(Long id) {
        this.id = id;
    }

    // Constructor with basic student details
    public StudentDto(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone, String emergencyContact, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.address = address;
    }

    public StudentDto(Long id, String firstName, String lastName, LocalDate dateOfBirth, String email, String phone, String emergencyContact, String address, DepartmentDto departmentDto, CourseDto courseDto, List<AssessmentDto> assessmentDto, RegistrationDto registrationDto, List<ModuleDto> moduleDto) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.address = address;
        this.department = departmentDto;
        this.course = courseDto;
        this.assessment = assessmentDto;
        this.registration = registrationDto;
        this.module = moduleDto;
    }
}
