package com.example.scr.repository;

import com.example.scr.domain.entity.Enrollment;
import com.example.scr.domain.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    // Add custom queries when needed, e.g. by student, by course, etc.
}
