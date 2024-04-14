package com.managementsystem.studentmanagement.services;

import com.managementsystem.studentmanagement.domains.*;
import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.dto.RegistrationDto;
import com.managementsystem.studentmanagement.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentDetailsRepository assessmentDetailsRepository;

    public ResponseEntity<String> registerStudentForCourse(Long studentId, RegistrationDto registrationDto) {
        try {
            // Retrieve the student by ID
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

            // Convert DTO to Entity for Course and Department
            Department department = departmentRepository.findById(registrationDto.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found with ID: " + registrationDto.getDepartment().getId()));

            Course course = courseRepository.findById(registrationDto.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + registrationDto.getCourse().getId()));

            // Check if a registration already exists for the student and course
            List<Registration> existingRegistrations = registrationRepository.findByStudentAndCourse(student, course);
            if (!existingRegistrations.isEmpty()) {
                throw new RuntimeException("Student already registered for the course");
            }

            // Fetch modules associated with the given course
            List<Module> modules = moduleRepository.findByCourseId(course.getId());

            // Check if any modules were not found for the provided course
            if (modules.isEmpty()) {
                throw new RuntimeException("No modules found for the provided course");
            }

            // Set the department and course for the student
            student.setDepartment(department);
            student.setCourse(course);

            // Associate modules with the student
            student.setModules(modules);

            // Create a new registration entity with UUID
            Registration registration = new Registration();
            registration.setId(UUID.randomUUID());
            registration.setRegistrationDate(registrationDto.getRegistrationDate());
            registration.setRegistrationMethod(registrationDto.getRegistrationMethod());
            registration.setRegistrationSource(registrationDto.getRegistrationSource());
            registration.setStudent(student);
            registration.setCourse(course);
            registration.setDepartment(department);

            // Associate modules with the registration
            registration.setModules(modules);

            // Save the registration to the database
            registration = registrationRepository.save(registration);

            for (Module module : modules) {
                // Fetch assessments for the given module
                List<Assessment> assessments = assessmentRepository.findByModule(module);

                for (Assessment assessment : assessments) {
                    // Create new AssessmentDetails for the module, student, and registration
                    AssessmentDetails assessmentDetails = new AssessmentDetails();
                    assessmentDetails.setStudent(student);
                    assessmentDetails.setRegistration(registration);
                    assessmentDetails.setAssessment(assessment); // Set the assessment

                    // Save the assessmentDetails
                    assessmentDetailsRepository.save(assessmentDetails);
                }
            }

            return ResponseEntity.ok().body("{\"message\": \"Registration complete successfully!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register student for course: " + e.getMessage());
        }
    }
    public Registration updateRegistrationDetails(UUID registrationId, RegistrationDto updatedRegistrationDto) {
        Registration existingRegistration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found with ID: " + registrationId));

        if (updatedRegistrationDto.getRegistrationDate() != null) {
            existingRegistration.setRegistrationDate(updatedRegistrationDto.getRegistrationDate());
        }

        if (updatedRegistrationDto.getRegistrationMethod() != null) {
            existingRegistration.setRegistrationMethod(updatedRegistrationDto.getRegistrationMethod());
        }

        if (updatedRegistrationDto.getRegistrationSource() != null) {
            existingRegistration.setRegistrationSource(updatedRegistrationDto.getRegistrationSource());
        }

        if (updatedRegistrationDto.getCourse() != null && updatedRegistrationDto.getCourse().getId() != null) {
            Course newCourse = courseRepository.findById(updatedRegistrationDto.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + updatedRegistrationDto.getCourse().getId()));

            Student associatedStudent = existingRegistration.getStudent();

            if (associatedStudent != null) {
                associatedStudent.setCourse(newCourse);
                associatedStudent.setDepartment(newCourse.getDepartment());
                associatedStudent.getModules().clear();
                associatedStudent.getModules().addAll(newCourse.getModules());

                studentRepository.save(associatedStudent);

                // Remove assessment details associated with old modules
                for (com.managementsystem.studentmanagement.domains.Module oldModule : existingRegistration.getModules()) {
                    List<Assessment> assessments = oldModule.getAssessments();
                    for (Assessment assessment : assessments) {
                        List<AssessmentDetails> assessmentDetailsList = assessmentDetailsRepository.findByRegistrationIdAndAssessmentId(existingRegistration.getId(), assessment.getId());
                        for (AssessmentDetails assessmentDetail : assessmentDetailsList) {
                            assessmentDetailsRepository.delete(assessmentDetail);
                        }
                    }
                }

                existingRegistration.setCourse(newCourse);
                existingRegistration.setDepartment(newCourse.getDepartment());
                existingRegistration.getModules().clear();
                existingRegistration.getModules().addAll(newCourse.getModules());

                // Update assessmentDetails for new modules
                for (Module newModule : newCourse.getModules()) {
                    List<Assessment> assessments = newModule.getAssessments();
                    for (Assessment assessment : assessments) {
                        AssessmentDetails newAssessmentDetail = new AssessmentDetails();
                        newAssessmentDetail.setRegistration(existingRegistration);
                        newAssessmentDetail.setAssessment(assessment);
                        newAssessmentDetail.setStudent(associatedStudent);

                        assessmentDetailsRepository.save(newAssessmentDetail);
                    }
                }
            }
        }

        return registrationRepository.save(existingRegistration);
    }

}
