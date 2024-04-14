package com.managementsystem.studentmanagement.enums;

public enum AssessmentTypes {

    IN_CLASS_TEST("In-class Test"),
    COURSEWORK("Coursework"),
    OPEN_BOOK_TEST("Open Book Test"),
    PERFORMANCE_BASED_TEST("Performance-based Test");

    public final String assessmentType;

    private AssessmentTypes(String assessmentType) {
        this.assessmentType = assessmentType;

    }


}
