CREATE TYPE property_type AS ENUM ('HOUSE', 'APARTMENT', 'LAND');
CREATE TYPE property_status AS ENUM ('ON_SALE', 'SOLD');

CREATE TABLE listings(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description text,
    price NUMERIC(12,2) NOT NULL CHECK ( price > 0 ),
    city VARCHAR(100) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    area NUMERIC(10,2) NOT NULL CHECK ( area > 0 ),
    bedrooms smallint CHECK ( bedrooms IS NULL OR bedrooms > 0),
    bathrooms smallint CHECK ( bathrooms IS NULL OR bathrooms > 0 ),
    type property_type NOT NULL,
    status property_status NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE amenity(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE listing_amenity(
    listing_id integer REFERENCES listings(id) ON DELETE CASCADE NOT NULL,
    amenity_id integer REFERENCES amenity(id) ON DELETE RESTRICT NOT NULL,
    PRIMARY KEY(listing_id, amenity_id) -- Ensures unique combination of IDs and creates index
);

