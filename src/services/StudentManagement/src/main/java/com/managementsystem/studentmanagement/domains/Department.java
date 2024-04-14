package com.managementsystem.studentmanagement.domains;

import com.managementsystem.studentmanagement.enums.DepartmentNames;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "department_name")
    private DepartmentNames name;

    @Setter
    @Getter
    @Column(name = "person_in-charge")
    private String inCharge;

    @OneToMany(mappedBy = "department")
    private List<Student> students;

    @Setter
    @Getter
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();


}
