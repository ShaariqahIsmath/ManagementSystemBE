package com.managementsystem.studentmanagement.repositories;

import com.managementsystem.studentmanagement.domains.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Module findByCode(String code);

    List<Module> findByCodeIn(List<String> codes);

    List<Module> findByCourseId(Long courseId);



}
