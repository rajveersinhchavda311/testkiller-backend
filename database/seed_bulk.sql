-- ============================================================================
-- TestKiller — seed_bulk.sql   (SCALABILITY seed: high volume)
-- Target  : TiDB Cloud Serverless (MySQL-compatible)
-- Run via : TiDB Cloud console -> SQL Editor -> paste -> Run
-- Order   : Run AFTER test.sql (the questions block needs >= 1 subject to exist).
--
-- Generates ~10,000 students + ~5,000 questions using set-based INSERT...SELECT.
-- No row-by-row loops and no stored procedures / DELIMITER — so it pastes
-- cleanly into the TiDB web SQL Editor and runs server-side in one shot.
--
-- Row count is produced by cross-joining four 10-row digit tables (10^4 = 10000)
-- giving n = 1..10000. IDs are auto_increment (won't collide with test.sql).
-- All bulk students share password: Student@123
-- Emails: bulkstudent1@seed.testkiller.com ... bulkstudent10000@seed.testkiller.com
-- ============================================================================

USE testkiller_db;

-- ---- 10,000 students --------------------------------------------------------
INSERT INTO users (first_name, last_name, email, password, phone_number, role, active, created_at)
SELECT
    CONCAT('Student', t.n),
    CONCAT('Bulk', t.n),
    CONCAT('bulkstudent', t.n, '@seed.testkiller.com'),
    '$2a$10$V6qAsFP9MZ0EQlymZ0zcYO4XgbCJl4zcB4Bpfsd7Vhg603M7GOWNu',  -- Student@123
    CONCAT('9', LPAD(t.n, 9, '0')),
    'STUDENT',
    b'1',
    NOW(6)
FROM (
    SELECT (d1.d + d2.d*10 + d3.d*100 + d4.d*1000 + 1) AS n
    FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d1
    CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d2
    CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d3
    CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d4
) t;

-- ---- 5,000 questions (attached to the first existing subject) ---------------
INSERT INTO questions (question_text, question_type, subject_id, topic, difficulty, marks, negative_marks, created_at, updated_at)
SELECT
    CONCAT('Auto-generated practice question #', t.n, ' — choose the correct output.'),
    'SINGLE_CHOICE',
    (SELECT MIN(id) FROM subjects),
    'Bulk',
    CASE WHEN t.n % 3 = 0 THEN 'EASY' WHEN t.n % 3 = 1 THEN 'MEDIUM' ELSE 'HARD' END,
    1,
    0,
    NOW(6),
    NOW(6)
FROM (
    SELECT (d1.d + d2.d*10 + d3.d*100 + d4.d*1000 + 1) AS n
    FROM (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d1
    CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d2
    CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d3
    CROSS JOIN (SELECT 0 d UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d4
) t
WHERE t.n <= 5000;

-- ---- Verify counts ----------------------------------------------------------
SELECT COUNT(*) AS total_students  FROM users     WHERE role = 'STUDENT';
SELECT COUNT(*) AS total_questions FROM questions;
