package com.managementsystem.studentmanagement.controllers;

import com.managementsystem.studentmanagement.domains.AssessmentDetails;
import com.managementsystem.studentmanagement.domains.Registration;
import com.managementsystem.studentmanagement.domains.Student;
import com.managementsystem.studentmanagement.dto.StudentDto;
import com.managementsystem.studentmanagement.repositories.AssessmentDetailsRepository;
import com.managementsystem.studentmanagement.repositories.RegistrationRepository;
import com.managementsystem.studentmanagement.repositories.StudentRepository;
import com.managementsystem.studentmanagement.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        try {
            studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student profile created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create student profile!");
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> getStudentDetails(@PathVariable Long studentId) {
        StudentDto studentDto = studentService.getStudentDetails(studentId);
        if (studentDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentDto);
    }


    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudentsDetails() {
        List<StudentDto> studentsWithDetails = studentService.getAllStudents();
        return ResponseEntity.ok(studentsWithDetails);
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
        return studentService.deleteStudent(studentId);
    }

    @PutMapping("/{studentId}/update")
    public ResponseEntity<String> updateStudentDetails(
            @PathVariable Long studentId,
            @RequestBody StudentDto updatedStudentDto) {
        return studentService.updateStudentDetails(studentId, updatedStudentDto);
    }

}
