package com.managementsystem.studentmanagement.repositories;

import com.managementsystem.studentmanagement.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface StudentRepository extends JpaRepository<Student, Long> {

}
