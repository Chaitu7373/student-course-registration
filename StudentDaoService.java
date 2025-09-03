package com.example.scr.service;

import com.example.scr.domain.dto.StudentCreateRequest;
import com.example.scr.domain.entity.*;
import com.example.scr.repository.CourseRepository;
import com.example.scr.repository.EnrollmentRepository;
import com.example.scr.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class StudentDaoService {

    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;
    public StudentDaoService(StudentRepository studentRepo,
                             CourseRepository courseRepo,
                             EnrollmentRepository enrollmentRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }
    /**
     * 2.1 Add a new student along with their course registrations (atomic).
     */
    @Transactional
    public Student addStudentWithCourses(StudentCreateRequest req) {
        // Create student (no Lombok builder)
        Student student = new Student();
        student.setName(req.name());
        student = studentRepo.save(student);

        // Fetch courses
        List<Course> courses = courseRepo.findAllById(req.courseIds());
        if (courses.size() != req.courseIds().size()) {
            throw new EntityNotFoundException("One or more course IDs not found");
        }

        // Create enrollments (no Lombok builder)
        for (Course c : courses) {
            EnrollmentId id = new EnrollmentId(student.getId(), c.getId());
            Enrollment e = new Enrollment();
            e.setId(id);
            e.setStudent(student);
            e.setCourse(c);
            e.setScore(null);
            enrollmentRepo.save(e);
        }

        return student;
    }
    /**
     * 2.2 Delete a student (removes enrollments via FK ON DELETE CASCADE or orphanRemoval).
     */
    @Transactional
    public void deleteStudent(Long studentId) {
        if (!studentRepo.existsById(studentId)) {
            return; // or throw if you prefer
        }
        studentRepo.deleteById(studentId);
    }

    /**
     * 2.3 Get all students, sorted by their name, for a given course name.
     */
    @Transactional
    public List<Student> getStudentsByCourseNameSorted(String courseName) {
        return studentRepo.findStudentsByCourseNameSorted(courseName);
    }

    /**
     * Bonus 2.4: Record/Update score for a student's course.
     */
    @Transactional
    public void upsertScore(Long studentId, Long courseId, Double score) {
        EnrollmentId id = new EnrollmentId(studentId, courseId);
        Enrollment e = enrollmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        e.setScore(score);
        enrollmentRepo.save(e);
    }

    /**
     * Bonus 2.5: Find students not registered for a given course name.
     */
    @Transactional
    public List<Student> findStudentsNotInCourse(String courseName) {
        return studentRepo.findStudentsNotInCourse(courseName);
    }
}
