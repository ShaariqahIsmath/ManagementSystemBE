package com.managementsystem.studentmanagement.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    private LocalDate dateOfBirth;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    private Registration registration;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_module",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "module_code")
    )
    private List<Module> modules = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentDetails> assessmentDetailsList = new ArrayList<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Attendance> attendanceRecords;


    public Student() {
    }

    public Student(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone, String emergencyContact, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.address = address;
    }

    public Student(Long id, String firstName, String lastName, LocalDate dateOfBirth, String email, String phone, String emergencyContact, String address, Department department, Course course) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.address = address;
        this.department = department;
        this.course = course;
    }
}
