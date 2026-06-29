# Database seed files

Seed data for the TestKiller backend, targeting **TiDB Cloud Serverless**
(MySQL-compatible). Run them from the TiDB Cloud console → **SQL Editor**
(paste → Run), against the `testkiller_db` database.

> The schema itself is created automatically by Hibernate (`ddl-auto=update`)
> the first time the app boots. These files only insert data.

## Files

| File | Purpose | Rows |
|------|---------|------|
| `test.sql` | Functional seed — small, readable, deterministic IDs. | 1 admin, 5 students, 2 courses, 4 subjects, 5 questions (+10 options), 2 exams, 1 batch |
| `seed_bulk.sql` | Scalability seed — set-based bulk insert. **Run after `test.sql`.** | ~10,000 students + ~5,000 questions |

## Run order

1. `test.sql`  — creates the admin you log in with, plus reference data.
2. `seed_bulk.sql` — optional, for the scalability/load check. Depends on at
   least one subject existing (created by `test.sql`).

## Login credentials created by `test.sql`

| Role | Email | Password |
|------|-------|----------|
| ADMIN | `admin@testkiller.com` | `Admin@123` |
| STUDENT | `aarav@student.testkiller.com` (and diya / rohan / isha / kabir) | `Student@123` |

Bulk students use the pattern `bulkstudent<N>@seed.testkiller.com`, all with
password `Student@123`.

## Notes / design choices

- Booleans are `bit(1)` in the generated schema, so they are written as
  `b'1'` / `b'0'` (not `1` / `0`, which can throw data-truncation errors).
- `created_at` is `NOT NULL` with no DB default (it is normally set by the
  entity's `@PrePersist`), so raw SQL sets it explicitly with `NOW(6)`.
- `seed_bulk.sql` uses a cross-join of four 10-row digit tables to generate
  10,000 rows in a single `INSERT ... SELECT` — no stored procedures or
  `DELIMITER`, which the TiDB web SQL Editor does not support.
- Passwords are bcrypt `$2a$10$` hashes, compatible with Spring Security's
  `BCryptPasswordEncoder`.
