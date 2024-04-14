package com.managementsystem.studentmanagement.enums;

public enum DepartmentNames {

    COMPUTER_SCIENCE("Computer Science"),
    BUSINESS("Business"),
    DATA_SCIENCE("Data Science"),
    ARCHITECTURE("Architecture"),
    CYBER_SECURITY("Cyber Security"),
    FASHION_DESIGN("Fashion Design");


    public final String departmentName;

    private DepartmentNames(String departmentName) {
        this.departmentName = departmentName;
    }

}
