-- name: findById
SELECT *
FROM users
WHERE id = :id;

-- name: findByUsername
SELECT *
FROM users
WHERE username = :username;

-- name: save
INSERT INTO users(email, password, role, username)
VALUES(:email, :password, CAST(:role AS role), :username)
RETURNING id, username, password, email, role;