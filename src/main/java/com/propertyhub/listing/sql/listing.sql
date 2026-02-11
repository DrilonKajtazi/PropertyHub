-- name: findOneById
SELECT id, title, description, price, city, neighborhood, area,
       bedrooms, bathrooms, type, status, created_at, updated_at
FROM listings
WHERE id = :id;

-- name: findAmenitiesByListingId
SELECT a.id, a.name
FROM amenity a
         JOIN listing_amenity la ON la.amenity_id = a.id
WHERE la.listing_id = :id
ORDER BY a.name;

-- name: findAll
SELECT *
FROM listings
ORDER BY created_at DESC, id
LIMIT :limit
OFFSET :offset;

-- name: findAllWithIds
SELECT *
FROM listings
WHERE listings.id IN (:ids);

-- name: findAmenitiesForListings
SELECT amenity.id, amenity.name, listing_amenity.listing_id
FROM amenity
JOIN listing_amenity ON amenity.id = listing_amenity.amenity_id
WHERE listing_id IN (:ids);

-- name: save
INSERT INTO listings(title, description, price, city,
                     neighborhood, area, bedrooms, bathrooms,
                     type, status)
VALUES (:title, :description, :price, :city,
        :neighborhood, :area, :bedrooms, :bathrooms,
        CAST(:type AS property_type),CAST(:status AS property_status))
RETURNING *;

-- name: listing.addAmenities
INSERT INTO listing_amenity(listing_id, amenity_id)
SELECT :listing_id, amenity_ids
FROM unnest(CAST(:amenity_ids AS int[])) AS amenity_ids
ON CONFLICT DO NOTHING;