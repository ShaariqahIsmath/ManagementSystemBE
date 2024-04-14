package com.managementsystem.studentmanagement.repositories;

import com.managementsystem.studentmanagement.domains.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
