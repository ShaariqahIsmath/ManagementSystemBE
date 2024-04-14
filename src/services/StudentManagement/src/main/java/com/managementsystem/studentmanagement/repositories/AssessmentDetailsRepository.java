package com.managementsystem.studentmanagement.repositories;

import com.managementsystem.studentmanagement.domains.AssessmentDetails;
import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.domains.Registration;
import com.managementsystem.studentmanagement.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource
public interface AssessmentDetailsRepository extends JpaRepository<AssessmentDetails, Long> {

    @Query("SELECT ad FROM AssessmentDetails ad JOIN ad.assessment a WHERE a.module = :module AND ad.student = :student")
    Optional<AssessmentDetails> findByModuleAndStudent(@Param("module") Module module, @Param("student") Student student);

    List<AssessmentDetails> findByRegistration(Registration registration);

    List<AssessmentDetails> findByStudentId(Long studentId);

    Optional<AssessmentDetails> findByStudentIdAndAssessmentId(Long id, Long assessmentId);

    List<AssessmentDetails> findByRegistrationIdAndAssessmentId(UUID registrationId, Long assessmentId);





}
