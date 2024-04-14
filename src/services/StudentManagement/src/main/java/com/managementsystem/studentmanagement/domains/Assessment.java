package com.managementsystem.studentmanagement.domains;

import com.managementsystem.studentmanagement.enums.AssessmentTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_code")
    private Module module;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type")
    private AssessmentTypes name;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentDetails> assessmentDetailsList = new ArrayList<>();


}
