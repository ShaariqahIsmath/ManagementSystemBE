package com.managementsystem.studentmanagement.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "modules")
public class Module {

    @Id
    @Column(name = "module_code")
    private String code;

    @Column(name = "module_name")
    private String name;

    @Column(name = "module_duration")//eg: 1 semester long or 2 semesters long
    private String duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments = new ArrayList<>();


}
