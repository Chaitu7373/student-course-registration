package com.example.scr.controller;

import com.example.scr.domain.dto.StudentCreateRequest;
import com.example.scr.domain.entity.Student;
import com.example.scr.service.StudentDaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentDaoService service;
    public StudentController(StudentDaoService service) {
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody StudentCreateRequest req) {
        return ResponseEntity.ok(service.addStudentWithCourses(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-course")
    public ResponseEntity<List<Student>> byCourse(@RequestParam String name) {
        return ResponseEntity.ok(service.getStudentsByCourseNameSorted(name));
    }

    @GetMapping("/not-in-course")
    public ResponseEntity<List<Student>> notInCourse(@RequestParam String name) {
        return ResponseEntity.ok(service.findStudentsNotInCourse(name));
    }
}
