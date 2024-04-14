package com.managementsystem.adminmanagement.repository;

import com.managementsystem.adminmanagement.models.ERole;
import com.managementsystem.adminmanagement.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
