package com.example.scr.repository;

import com.example.scr.domain.entity.Course;
import com.example.scr.domain.entity.Student;
import com.example.scr.domain.entity.Enrollment;
import com.example.scr.domain.entity.EnrollmentId;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // 2.3 Get all students sorted by name for a given course name.
    @Query("""
            SELECT s
            FROM Enrollment e
              JOIN e.student s
              JOIN e.course c
            WHERE c.name = :courseName
            ORDER BY s.name ASC
           """)
    List<Student> findStudentsByCourseNameSorted(@Param("courseName") String courseName);

    // 2.5 Students NOT registered for a given course name
    @Query("""
            SELECT s
            FROM Student s
            WHERE NOT EXISTS (
               SELECT 1 FROM Enrollment e
               JOIN e.course c
               WHERE e.student = s AND c.name = :courseName
            )
            ORDER BY s.name ASC
           """)
    List<Student> findStudentsNotInCourse(@Param("courseName") String courseName);
}

/*
2.4). What if we want to record course scores? What possible changes need to be made? Explain briefly.
Here’s the clean way to add scores per student per course.

What changes?
1) Schema (DDL)

Scores live on the join between Student and Course (one score per enrollment).
-- If the table already exists:
ALTER TABLE enrollments
  ADD COLUMN score NUMERIC(5,2),
  ADD CONSTRAINT chk_enr_score CHECK (score IS NULL OR (score >= 0 AND score <= 100));
-- (Choose your own range/scale: e.g., 0–4.00 GPA or 0–100%)
Why join table? Because a score belongs to the relationship, not to the student or course alone.

2) Entity
Add a field to the Enrollment entity:

// in Enrollment.java
@Column(precision = 5, scale = 2)   // matches NUMERIC(5,2)
private Double score;               // nullable until graded

// (Optional validation if you expose via DTO)
// @DecimalMin("0.0") @DecimalMax("100.0")


3) Repository / Service (DAO)

Add a small “upsert score” use-case:
// StudentDaoService.java
@Transactional
public void upsertScore(Long studentId, Long courseId, Double score) {
    EnrollmentId id = new EnrollmentId(studentId, courseId);
    Enrollment e = enrollmentRepo.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    e.setScore(score);
    enrollmentRepo.save(e);
}

4) API (optional)
@PatchMapping("/{studentId}/courses/{courseId}/score")
public ResponseEntity<Void> score(
   @PathVariable Long studentId,
   @PathVariable Long courseId,
   @RequestParam Double score) {
  service.upsertScore(studentId, courseId, score);
  return ResponseEntity.noContent().build();
}


Best-practice notes

If you originally modeled ManyToMany without a join entity, you must refactor to an explicit Enrollment entity to hold score.

Use Flyway migration (e.g., V2__add_score_to_enrollments.sql) rather than ddl-auto in real environments.

Add a CHECK constraint for valid range; also validate in the API layer.

Consider optimistic locking (@Version) if multiple graders can update the same score.

If you need history/audit, add a separate enrollment_scores_history table (enrollment_id, score, changed_at, changed_by).

That’s it—one column, one field, one service method, optionally one endpoint.
 */
