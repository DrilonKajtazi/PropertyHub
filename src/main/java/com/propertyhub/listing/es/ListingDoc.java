package com.propertyhub.listing.es;

import com.propertyhub.amenity.entity.Amenity;
import com.propertyhub.listing.entity.Listing;
import com.propertyhub.listing.model.PropertyStatus;
import com.propertyhub.listing.model.PropertyType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@Document(indexName = "listings")
public class ListingDoc {
    @Id @Getter                        private Long id;

    @Field(type = FieldType.Text)      private String title;
    @Field(type = FieldType.Text)      private String description;

    @Field(type = FieldType.Keyword)   private String city;
    @Field(type = FieldType.Keyword)   private String neighborhood;
    @Field(type = FieldType.Keyword)   private PropertyType type;
    @Field(type = FieldType.Keyword)   private PropertyStatus status;
    @Field(type = FieldType.Keyword)   private List<String> amenities;

    @Field(type = FieldType.Double)    private BigDecimal price;
    @Field(type = FieldType.Double)    private BigDecimal area;

    @Field(type = FieldType.Short)     private Integer bedrooms;
    @Field(type = FieldType.Short)     private Integer bathrooms;

    @Field(type = FieldType.Date)      private Date createdAt;
    @Field(type = FieldType.Date)      private Date updatedAt;

    public static ListingDoc from(Listing l) {
        List<String> amenities = l.getAmenities().stream().map(Amenity::getName).toList();
        return ListingDoc.builder()
                .id(l.getId())
                .title(l.getTitle())
                .description(l.getDescription())
                .price(l.getPrice())
                .city(l.getCity())
                .neighborhood(l.getNeighborhood())
                .area(l.getArea())
                .bedrooms(l.getBedrooms())
                .bathrooms(l.getBathrooms())
                .type(l.getType())
                .status(l.getStatus())
                .amenities(amenities)
                .createdAt(l.getCreatedAt())
                .updatedAt(l.getUpdatedAt())
                .build();
    }
}
