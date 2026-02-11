package com.propertyhub.listing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListingRequest {
    private String title;
    private String description;

    private String city;
    private String neighborhood;
    private PropertyType type;
    private PropertyStatus status;
    private List<Long> amenitiesIds;

    private BigDecimal price;
    private BigDecimal area;

    private Integer bedrooms;
    private Integer bathrooms;
}
