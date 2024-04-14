package com.managementsystem.studentmanagement.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.managementsystem.studentmanagement.enums.CourseTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "course_type")
    @Enumerated(EnumType.STRING)
    private CourseTypes type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Module> modules;


}
