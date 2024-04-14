package com.managementsystem.studentmanagement.repositories;

import com.managementsystem.studentmanagement.domains.Assessment;
import com.managementsystem.studentmanagement.domains.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findByModule(Module module);

}
