package com.propertyhub.listing.entity;

import com.propertyhub.amenity.entity.Amenity;
import com.propertyhub.listing.model.PropertyStatus;
import com.propertyhub.listing.model.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Listing {
    private Long id;
    private String title;
    private String description;

    private String city;
    private String neighborhood;
    private PropertyType type;
    private PropertyStatus status;
    private List<Amenity> amenities;

    private BigDecimal price;
    private BigDecimal area;

    private Integer bedrooms;
    private Integer bathrooms;

    private Date createdAt;
    private Date updatedAt;
}
