CREATE TABLE IF NOT EXISTS addresses (
                           id SERIAL PRIMARY KEY,
                           street VARCHAR(255) NOT NULL,
                           number INTEGER NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           zip_code VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       fullname VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       user_type VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       address_id INTEGER NOT NULL,
                       CONSTRAINT fk_address
                           FOREIGN KEY(address_id)
                               REFERENCES addresses(id)
                               ON DELETE CASCADE
);

INSERT INTO addresses (street, number, city, zip_code)
SELECT 'Rua 1', 1, 'Cidade 1', '12345678'
    WHERE NOT EXISTS (
    SELECT 1
    FROM addresses
    WHERE street = 'Rua 1'
      AND number = 1
      AND city = 'Cidade 1'
      AND zip_code = '12345678'
);

INSERT INTO users (
    fullname,
    email,
    password_hash,
    user_type,
    created_at,
    updated_at,
    address_id
)
SELECT
    'Administrador',
    'admin@admin.com',
    '$2a$10$Lf.G9iCM9hE/bQt8R.fHCu5PI0hzDDRJxOrHLKKAoYyUXAM8JsmPC',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    a.id
FROM addresses a
WHERE a.street = 'Rua 1'
  AND a.number = 1
  AND a.city = 'Cidade 1'
  AND a.zip_code = '12345678'
  AND NOT EXISTS (
    SELECT 1
    FROM users
    WHERE email = 'admin@admin.com'
);