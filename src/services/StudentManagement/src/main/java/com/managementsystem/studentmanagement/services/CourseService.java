package com.managementsystem.studentmanagement.services;

import com.managementsystem.studentmanagement.domains.Course;
import com.managementsystem.studentmanagement.domains.Department;
import com.managementsystem.studentmanagement.dto.CourseDto;
import com.managementsystem.studentmanagement.dto.DepartmentDto;
import com.managementsystem.studentmanagement.enums.CourseTypes;
import com.managementsystem.studentmanagement.repositories.CourseRepository;
import com.managementsystem.studentmanagement.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public CourseDto addCourse(CourseDto courseDto) {
        if (courseDto == null || courseDto.getDepartment() == null || courseDto.getDepartment().getId() == null) {
            throw new IllegalArgumentException("Invalid course data");
        }

        Department department = departmentRepository.findById(courseDto.getDepartment().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid department ID"));

        Course course = new Course();
        course.setName(courseDto.getName());

        boolean isValidCourseType = false;
        for (CourseTypes type : CourseTypes.values()) {
            if (type.courseType.equalsIgnoreCase(courseDto.getType())) {
                course.setType(type);
                isValidCourseType = true;
                break;
            }
        }

        if (!isValidCourseType) {
            throw new IllegalArgumentException("Invalid course type");
        }

        course.setDepartment(department);

        Course savedCourse = courseRepository.save(course);
        return mapToDto(savedCourse);
    }


    private CourseDto mapToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setType(course.getType().toString());

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(course.getDepartment().getId());
        courseDto.setDepartment(departmentDto);

        return courseDto;
    }

    public List<Map<String, Object>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream()
                .map(course -> {
                    Map<String, Object> courseMap = new HashMap<>();
                    courseMap.put("id", course.getId());
                    courseMap.put("name", course.getName());
                    courseMap.put("type", course.getType());
                    return courseMap;
                })
                .collect(Collectors.toList());
    }



}
