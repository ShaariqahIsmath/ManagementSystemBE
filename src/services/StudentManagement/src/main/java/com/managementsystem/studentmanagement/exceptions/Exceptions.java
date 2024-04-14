package com.managementsystem.studentmanagement.exceptions;

import lombok.Getter;

@Getter
public enum Exceptions implements StatusCode {
    DEPARTMENT_NOT_FOUND(404, "Department not found"),
    COURSE_NOT_FOUND(404, "Course not found"),
    STUDENT_NOT_FOUND(404, "Student not found"),
    STUDENT_NOT_FOUND_WITH_ID(404, "Student not found with ID"),
    REGISTRATION_NOT_FOUND(404, "Registration not found"),
    REGISTRATION_NOT_FOUND_WITH_ID(404, "Registration not found with ID"),
    REGISTRATION_NOT_FOUND_WITH_STUDENT_ID(404, "Registration not found with Student ID"),
    ASSESSMENT_NOT_FOUND(404, "Assessment not found"),
    ASSESSMENT_NOT_FOUND_WITH_ID(404, "Assessment not found with ID"),
    SUCCESS(200, "Operation successful"),
    ;

    private final int code;
    private final String message;

    Exceptions(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
