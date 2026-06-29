-- ============================================================================
-- TestKiller — test.sql   (FUNCTIONAL seed: small, readable)
-- Target  : TiDB Cloud Serverless (MySQL-compatible)
-- Run via : TiDB Cloud console -> SQL Editor -> paste -> Run
-- Notes   : Safe on a fresh/empty schema. Uses explicit IDs for deterministic
--           foreign keys. Booleans are bit(1) -> written as b'1' / b'0'.
--           created_at is NOT NULL with no DB default, so it is set explicitly.
--
-- LOGIN CREDENTIALS created by this file (use at /api/v1/public/auth/login):
--   ADMIN    email: admin@testkiller.com            password: Admin@123
--   STUDENT  email: aarav@student.testkiller.com    password: Student@123
--            (diya / rohan / isha / kabir @student.testkiller.com share Student@123)
-- ============================================================================

USE testkiller_db;

-- Optional clean slate — uncomment to reset (children first, FK order):
-- DELETE FROM batch_students;
-- DELETE FROM exam_questions;
-- DELETE FROM question_options;
-- DELETE FROM exams;
-- DELETE FROM questions;
-- DELETE FROM subjects;
-- DELETE FROM batches;
-- DELETE FROM users;
-- DELETE FROM courses;

-- ---- Courses ----------------------------------------------------------------
INSERT INTO courses (id, name, description, duration, active, created_at, updated_at) VALUES
  (1, 'Full Stack Web Development', 'MERN + Spring Boot track', '6 months', b'1', NOW(6), NOW(6)),
  (2, 'Data Science',               'Python, ML and statistics', '8 months', b'1', NOW(6), NOW(6));

-- ---- Subjects (FK -> courses) -----------------------------------------------
INSERT INTO subjects (id, name, description, course_id, created_at, updated_at) VALUES
  (1, 'Java',        'Core Java and OOP',          1, NOW(6), NOW(6)),
  (2, 'Spring Boot', 'REST APIs with Spring Boot', 1, NOW(6), NOW(6)),
  (3, 'Python',      'Python programming',         2, NOW(6), NOW(6)),
  (4, 'Statistics',  'Probability and statistics', 2, NOW(6), NOW(6));

-- ---- Admin user (role ADMIN) — password = Admin@123 -------------------------
INSERT INTO users (id, first_name, last_name, email, password, phone_number, gender, date_of_birth, role, active, course_id, created_at) VALUES
  (1, 'System', 'Admin', 'admin@testkiller.com',
      '$2a$10$w7RqF6CbjnUBuZbcwOsti.5h1WAHcQPneVXlTOfqBWXARpv23.CYW',
      '9000000000', 'OTHER', '1990-01-01', 'ADMIN', b'1', NULL, NOW(6));

-- ---- Students (role STUDENT) — password = Student@123 -----------------------
INSERT INTO users (id, first_name, last_name, email, password, phone_number, gender, date_of_birth, role, active, course_id, created_at) VALUES
  (2, 'Aarav', 'Sharma', 'aarav@student.testkiller.com', '$2a$10$V6qAsFP9MZ0EQlymZ0zcYO4XgbCJl4zcB4Bpfsd7Vhg603M7GOWNu', '9000000002', 'MALE',   '2002-05-14', 'STUDENT', b'1', 1, NOW(6)),
  (3, 'Diya',  'Patel',  'diya@student.testkiller.com',  '$2a$10$V6qAsFP9MZ0EQlymZ0zcYO4XgbCJl4zcB4Bpfsd7Vhg603M7GOWNu', '9000000003', 'FEMALE', '2003-08-22', 'STUDENT', b'1', 1, NOW(6)),
  (4, 'Rohan', 'Mehta',  'rohan@student.testkiller.com', '$2a$10$V6qAsFP9MZ0EQlymZ0zcYO4XgbCJl4zcB4Bpfsd7Vhg603M7GOWNu', '9000000004', 'MALE',   '2001-12-01', 'STUDENT', b'1', 2, NOW(6)),
  (5, 'Isha',  'Gupta',  'isha@student.testkiller.com',  '$2a$10$V6qAsFP9MZ0EQlymZ0zcYO4XgbCJl4zcB4Bpfsd7Vhg603M7GOWNu', '9000000005', 'FEMALE', '2002-03-30', 'STUDENT', b'1', 2, NOW(6)),
  (6, 'Kabir', 'Singh',  'kabir@student.testkiller.com', '$2a$10$V6qAsFP9MZ0EQlymZ0zcYO4XgbCJl4zcB4Bpfsd7Vhg603M7GOWNu', '9000000006', 'MALE',   '2000-07-19', 'STUDENT', b'1', NULL, NOW(6));

-- ---- Questions --------------------------------------------------------------
INSERT INTO questions (id, question_text, question_type, subject_id, topic, difficulty, marks, negative_marks, explanation, tags, correct_answer, created_at, updated_at) VALUES
  (1, 'Which keyword is used to inherit a class in Java?',          'SINGLE_CHOICE',   1, 'Inheritance', 'EASY',   1, 0,   'The extends keyword establishes inheritance.',                       'java,oop',            NULL, NOW(6), NOW(6)),
  (2, 'Which of the following are Spring stereotype annotations?',  'MULTIPLE_CHOICE', 2, 'Annotations', 'MEDIUM', 2, 0.5, '@Component and @Service are stereotypes, while @Entity is JPA.',      'spring,annotations',  NULL, NOW(6), NOW(6)),
  (3, 'Python is a dynamically typed language.',                    'TRUE_FALSE',      3, 'Basics',      'EASY',   1, 0,   'Types are checked at runtime in Python.',                            'python',              NULL, NOW(6), NOW(6)),
  (4, 'What is the result of 6 * 7?',                               'NUMERICAL',       4, 'Arithmetic',  'EASY',   1, 0,   '6 * 7 = 42.',                                                        'math',                '42', NOW(6), NOW(6)),
  (5, 'Explain the role of the JVM in Java execution.',             'DESCRIPTIVE',     1, 'JVM',         'HARD',   5, 0,   'Expect mention of bytecode, platform independence, JIT.',            'java,jvm',            NULL, NOW(6), NOW(6));

-- ---- Question options (FK -> questions) -------------------------------------
INSERT INTO question_options (question_id, option_text, correct, option_order) VALUES
  (1, 'extends',    b'1', 0),
  (1, 'implements', b'0', 1),
  (1, 'inherits',   b'0', 2),
  (1, 'super',      b'0', 3),
  (2, '@Component', b'1', 0),
  (2, '@Service',   b'1', 1),
  (2, '@Entity',    b'0', 2),
  (2, '@Override',  b'0', 3),
  (3, 'True',       b'1', 0),
  (3, 'False',      b'0', 1);

-- ---- Exams ------------------------------------------------------------------
INSERT INTO exams (id, title, description, instructions, subject_id, course_id, total_marks, passing_marks, duration, max_attempts, shuffle_questions, shuffle_options, negative_marking, status, created_at, updated_at) VALUES
  (1, 'Java Fundamentals Quiz', 'Basic Java assessment', 'Answer all questions. No negative marking.', 1, 1, 8, 3, 30, 1, b'0', b'0', b'0', 'ACTIVE', NOW(6), NOW(6)),
  (2, 'Data Science Basics',    'Intro DS assessment',   'Timed test of 45 minutes.',                  3, 2, 2, 1, 45, 2, b'1', b'1', b'1', 'DRAFT',  NOW(6), NOW(6));

-- ---- Exam <-> Question assignments (FK -> exams, questions) ------------------
INSERT INTO exam_questions (exam_id, question_id, question_order, marks_override) VALUES
  (1, 1, 1, NULL),
  (1, 2, 2, NULL),
  (1, 5, 3, NULL),
  (2, 3, 1, NULL),
  (2, 4, 2, NULL);

-- ---- Batches ----------------------------------------------------------------
INSERT INTO batches (id, name, start_date, end_date, capacity, faculty, created_at, updated_at) VALUES
  (1, 'FSWD-2026-Batch-A', '2026-01-15', '2026-07-15', 60, 'Dr. Rao', NOW(6), NOW(6));

-- ---- Batch <-> Student membership (FK -> batches, users) --------------------
INSERT INTO batch_students (batch_id, user_id) VALUES
  (1, 2),
  (1, 3);

-- ---- Quick verification -----------------------------------------------------
-- SELECT role, COUNT(*) FROM users GROUP BY role;
-- SELECT * FROM exams;
