package com.managementsystem.studentmanagement.repositories;

import com.managementsystem.studentmanagement.domains.Course;
import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.domains.Registration;
import com.managementsystem.studentmanagement.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Optional<Registration> findById(UUID registrationId);

    Optional<Registration> findByStudentId(Long studentId);

    @Query("SELECT r FROM Registration r WHERE r.student = :student AND r.course = :course")
    List<Registration> findByStudentAndCourse(@Param("student") Student student, @Param("course") Course course);

    Optional<Registration> findByStudent(Student student);






    List<Module> findByCourseId(Long courseId);

}
