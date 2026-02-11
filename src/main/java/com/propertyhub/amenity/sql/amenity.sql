-- name: save
INSERT INTO amenity(name)
VALUES (:name)
RETURNING *;

-- name: findOneById
SELECT * FROM amenity
WHERE id = :id;