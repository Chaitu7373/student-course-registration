-- STUDENTS
CREATE TABLE students (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(255) NOT NULL
);

-- COURSES
CREATE TABLE courses (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(255) NOT NULL UNIQUE
);

-- ENROLLMENTS (join table with room for attributes like score)
CREATE TABLE enrollments (
  student_id  BIGINT NOT NULL,
  course_id   BIGINT NOT NULL,
  -- bonus (2.4): optional score; nullable until we start recording it
  score       NUMERIC(5,2),
  PRIMARY KEY (student_id, course_id),
  CONSTRAINT fk_enr_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
  CONSTRAINT fk_enr_course  FOREIGN KEY (course_id)  REFERENCES courses(id)  ON DELETE RESTRICT
);

-- Helpful indexes
CREATE INDEX idx_enr_student ON enrollments(student_id);
CREATE INDEX idx_enr_course  ON enrollments(course_id);
