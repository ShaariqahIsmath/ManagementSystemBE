package com.managementsystem.studentmanagement.services;

import com.managementsystem.studentmanagement.domains.Module;
import com.managementsystem.studentmanagement.domains.*;
import com.managementsystem.studentmanagement.dto.*;
import com.managementsystem.studentmanagement.exceptions.Exceptions;
import com.managementsystem.studentmanagement.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentDetailsRepository assessmentDetailsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    public StudentDto getStudentDetails(Long studentId) {
        // Fetch student details
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException(Exceptions.STUDENT_NOT_FOUND_WITH_ID.getMessage() + ": " + studentId));

        // Fetch registration details
        Registration registration = registrationRepository.findByStudentId(studentId).orElse(null);

        // Fetch assessment details for the student
        List<AssessmentDetails> assessmentDetailsList = assessmentDetailsRepository.findByStudentId(studentId);

        // Extract unique assessments for the student from assessmentDetailsList
        Set<Assessment> uniqueAssessments = assessmentDetailsList.stream()
                .map(AssessmentDetails::getAssessment)
                .collect(Collectors.toSet());

        // Create and populate the StudentDto with all the fetched details
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setDateOfBirth(student.getDateOfBirth());
        studentDto.setEmail(student.getEmail());
        studentDto.setPhone(student.getPhone());
        studentDto.setEmergencyContact(student.getEmergencyContact());
        studentDto.setAddress(student.getAddress());

        // Populate department details
        Department department = (registration != null) ? registration.getDepartment() : null;
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId((department != null) ? department.getId() : null);
        departmentDto.setName((department != null) ? String.valueOf(department.getName()) : "Not Registered");
        departmentDto.setInCharge((department != null) ? department.getInCharge() : null);
        studentDto.setDepartment(departmentDto);

        Course course = (registration != null) ? registration.getCourse() : null;
        CourseDto courseDto = new CourseDto();
        courseDto.setId((course != null) ? course.getId() : null);
        courseDto.setType((course != null) ? String.valueOf(course.getType()) : null);
        courseDto.setName((course != null) ? course.getName() : null);
        studentDto.setCourse(courseDto);



        // Populate registration details
        if (registration != null) {
            RegistrationDto registrationDto = new RegistrationDto();
            registrationDto.setId(registration.getId());
            registrationDto.setRegistrationDate(registration.getRegistrationDate());
            registrationDto.setRegistrationMethod(registration.getRegistrationMethod());
            registrationDto.setRegistrationSource(registration.getRegistrationSource());
            studentDto.setRegistration(registrationDto);
        } else {
            RegistrationDto registrationDto = new RegistrationDto();
            registrationDto.setId(null);
            registrationDto.setRegistrationDate(null);
            registrationDto.setRegistrationMethod("Not Registered");
            registrationDto.setRegistrationSource("Not Registered");
            studentDto.setRegistration(registrationDto);
        }

        // Populate module details
        List<ModuleDto> moduleDtos = student.getModules().stream()
                .map(module -> {
                    ModuleDto moduleDto = new ModuleDto();
                    moduleDto.setCode(module.getCode());
                    moduleDto.setName(module.getName());
                    moduleDto.setDuration(module.getDuration());

                    // Filter assessments by module
                    List<AssessmentDto> assessmentDtosForModule = uniqueAssessments.stream()
                            .filter(assessment -> assessment.getModule().getCode().equals(module.getCode()))
                            .map(assessment -> {
                                AssessmentDto assessmentDto = new AssessmentDto();
                                assessmentDto.setId(assessment.getId());
                                assessmentDto.setModuleCode(module.getCode());
                                assessmentDto.setName(assessment.getName().toString());

                                // Populate AssessmentDetails for each assessment
                                List<AssessmentDetailsDto> assessmentDetailsDtos = assessmentDetailsList.stream()
                                        .filter(details -> details.getAssessment().getId().equals(assessment.getId()))
                                        .map(details -> {
                                            AssessmentDetailsDto detailsDto = new AssessmentDetailsDto();
                                            detailsDto.setId(details.getId());
                                            detailsDto.setMarks(details.getMarks());
                                            // Add other AssessmentDetails attributes as needed
                                            return detailsDto;
                                        })
                                        .collect(Collectors.toList());

                                assessmentDto.setAssessmentDetailsList(assessmentDetailsDtos);
                                return assessmentDto;
                            })
                            .collect(Collectors.toList());

                    moduleDto.setAssessments(assessmentDtosForModule);
                    return moduleDto;
                })
                .collect(Collectors.toList());

        studentDto.setModule(moduleDtos);

        return studentDto;
    }

    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private StudentDto convertToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setDateOfBirth(student.getDateOfBirth());
        studentDto.setEmail(student.getEmail());
        studentDto.setPhone(student.getPhone());
        studentDto.setEmergencyContact(student.getEmergencyContact());
        studentDto.setAddress(student.getAddress());

        // Fetch department details if present
        Department department = student.getDepartment();
        if (department != null) {
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setId(department.getId());
            departmentDto.setName(String.valueOf(department.getName()));
            studentDto.setDepartment(departmentDto);
        }

        // Fetch course details if present
        Course course = student.getCourse();
        if (course != null) {
            CourseDto courseDto = new CourseDto();
            courseDto.setId(course.getId());
            courseDto.setName(course.getName());
            studentDto.setCourse(courseDto);
        }

        // Fetch registration details
        Registration registration = registrationRepository.findByStudentId(student.getId())
                .orElse(null);
        if (registration != null) {
            RegistrationDto registrationDto = new RegistrationDto();
            registrationDto.setId(registration.getId());
            studentDto.setRegistration(registrationDto);
        }

        // Fetch modules selected by the student
        List<String> moduleCodes = jdbcTemplate.queryForList(
                "SELECT module_code FROM student.student_module WHERE student_id = ?",
                String.class,
                student.getId()
        );

        List<ModuleDto> moduleDtos = new ArrayList<>();
        for (String moduleCode : moduleCodes) {
            Module module = moduleRepository.findByCode(moduleCode);
            if (module != null) {
                ModuleDto moduleDto = new ModuleDto();
                moduleDto.setCode(module.getCode());
                moduleDto.setName(module.getName());
                moduleDtos.add(moduleDto);
            }
        }

        studentDto.setModule(moduleDtos);

        return studentDto;
    }

    public ResponseEntity<String> deleteStudent(Long studentId) {
        try {
            // Retrieve the student by ID
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

            // Retrieve the registration by student
            Registration registration = registrationRepository.findByStudent(student)
                    .orElseThrow(() -> new RuntimeException("Registration not found for student: " + studentId));

            // Delete associated assessment details
            List<AssessmentDetails> assessmentDetailsList = assessmentDetailsRepository.findByRegistration(registration);
            assessmentDetailsRepository.deleteAll(assessmentDetailsList);

            // Delete registration
            registrationRepository.delete(registration);

            // Delete student
            studentRepository.delete(student);

            return ResponseEntity.ok("Student deleted successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete student: " + e.getMessage());
        }
    }

    public ResponseEntity<String> updateStudentDetails(Long studentId, StudentDto updatedStudentDto) {
        try {
            // Fetch the existing student details based on studentId
            Optional<Student> existingStudentOpt = studentRepository.findById(studentId);

            if (existingStudentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Student existingStudent = existingStudentOpt.get();

            // Update the student details if the fields are provided
            if (updatedStudentDto.getFirstName() != null) {
                existingStudent.setFirstName(updatedStudentDto.getFirstName());
            }
            if (updatedStudentDto.getLastName() != null) {
                existingStudent.setLastName(updatedStudentDto.getLastName());
            }
            if (updatedStudentDto.getDateOfBirth() != null) {
                existingStudent.setDateOfBirth(updatedStudentDto.getDateOfBirth());
            }
            if (updatedStudentDto.getEmail() != null) {
                existingStudent.setEmail(updatedStudentDto.getEmail());
            }
            if (updatedStudentDto.getPhone() != null) {
                existingStudent.setPhone(updatedStudentDto.getPhone());
            }
            if (updatedStudentDto.getEmergencyContact() != null) {
                existingStudent.setEmergencyContact(updatedStudentDto.getEmergencyContact());
            }
            if (updatedStudentDto.getAddress() != null) {
                existingStudent.setAddress(updatedStudentDto.getAddress());
            }

            // Save/update the updated student details
            studentRepository.save(existingStudent);

            return ResponseEntity.ok("Student details updated successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update student details: " + e.getMessage());
        }
    }



}
