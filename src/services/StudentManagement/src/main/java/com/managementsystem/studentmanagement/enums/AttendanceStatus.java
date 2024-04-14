package com.managementsystem.studentmanagement.enums;

public enum AttendanceStatus {

    PRESENT("Present"),
    ABSENT("Absent"),
    LATE("Late"),
    EXCUSED_ABSENCE("Excused Absence");


    public final String attendanceStatus;

    private AttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;

    }
}
