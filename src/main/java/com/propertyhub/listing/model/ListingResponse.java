package com.propertyhub.listing.model;

import com.propertyhub.amenity.entity.Amenity;
import com.propertyhub.listing.entity.Listing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record ListingResponse(
        long id,
        String title,
        String description,
        String city,
        String neighborhood,
        PropertyType type,
        PropertyStatus status,
        List<String> amenities,
        BigDecimal price,
        BigDecimal area,
        Integer bedrooms,
        Integer bathrooms,
        Date createdAt,
        Date updatedAt
) {
    public static ListingResponse from(Listing l) {
        return new ListingResponse(
                l.getId(), l.getTitle(), l.getDescription(),
                l.getCity(), l.getNeighborhood(),
                l.getType(), l.getStatus(),
                l.getAmenities().stream().map(Amenity::getName).toList(),
                l.getPrice(), l.getArea(),
                l.getBedrooms(), l.getBathrooms(),
                l.getCreatedAt(), l.getUpdatedAt()
        );
    }
}

