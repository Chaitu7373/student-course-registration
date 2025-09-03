-- Courses (explicit IDs so we can reference them)
INSERT INTO courses (id, name) VALUES (1, 'Math'), (2, 'Physics'), (3, 'Chemistry');

-- Students
INSERT INTO students (id, name) VALUES
  (1, 'Alice'),
  (2, 'Bob'),
  (3, 'Charlie');

-- Enrollments (student_id, course_id, score)
INSERT INTO enrollments (student_id, course_id, score) VALUES
  (1, 1, 95.0),   -- Alice -> Math
  (1, 2, 88.5),   -- Alice -> Physics
  (2, 1, 76.0),   -- Bob   -> Math
  (3, 3, NULL);   -- Charlie -> Chemistry
