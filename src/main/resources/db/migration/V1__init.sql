CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

-- Password: "password" (bcrypt hash)
-- '$2a$10$1S44KaqBqQVwpp7KCrFZDOj90lezPxr2/ahBzvOi0x/NT9vffw70m'
INSERT INTO users (email, password, role)
VALUES ('admin@example.com', '$2a$10$1S44KaqBqQVwpp7KCrFZDOj90lezPxr2/ahBzvOi0x/NT9vffw70m', 'ROLE_ADMIN');
