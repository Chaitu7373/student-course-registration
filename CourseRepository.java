package com.example.scr.repository;

import com.example.scr.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);

    List<Course> findByNameIn(List<String> names);
}
