CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS addresses (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number INTEGER NOT NULL,
    city VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    user_id INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_user_address
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);

INSERT INTO users (
    fullname,
    email,
    password_hash,
    user_type,
    created_at,
    updated_at
)
SELECT
    'Administrador',
    'admin@admin.com',
    '$2a$10$Lf.G9iCM9hE/bQt8R.fHCu5PI0hzDDRJxOrHLKKAoYyUXAM8JsmPC',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
    WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE user_type = 'ADMIN'
);

INSERT INTO addresses (
    street,
    number,
    city,
    zip_code,
    user_id
)
SELECT
    'Rua 1',
    1,
    'Cidade 1',
    '12345678',
    u.id
FROM users u
WHERE u.user_type = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1
    FROM addresses a
    WHERE a.user_id = u.id
);