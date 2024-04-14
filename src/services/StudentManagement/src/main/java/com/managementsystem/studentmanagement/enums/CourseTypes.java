package com.managementsystem.studentmanagement.enums;

public enum CourseTypes {

    FOUNDATION("Foundation"),
    UNDERGRADUATE_STUDIES("Undergraduate Studies"),
    POSTGRADUATE_STUDIES("Postgraduate Studies");

    public String courseType;

    private CourseTypes(String courseType) {
        this.courseType = courseType;
    }
}
