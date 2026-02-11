package com.propertyhub.listing.repository;

import com.propertyhub.amenity.entity.Amenity;
import com.propertyhub.listing.entity.Listing;
import com.propertyhub.listing.model.ListingRequest;
import com.propertyhub.listing.model.PropertyStatus;
import com.propertyhub.listing.model.PropertyType;
import com.propertyhub.persistence.sql.SqlRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ListingRepository {
    private final JdbcClient jdbcClient;
    private final SqlRegistry sqlRegistry;

    public List<Listing> findAll(int limit, int offset) {
        List<Listing> listings = jdbcClient.sql(sqlRegistry.get("listing.findAll"))
                .param("limit", limit)
                .param("offset", offset)
                .query(this::mapRsToListing);

        List<Long> listingsIds = listings.stream().map(Listing::getId).toList();

        HashMap<Long, List<Amenity>> amenities = this.findAllAmenitiesWithIds(listingsIds);

        listings.forEach(listing -> {
            listing.setAmenities(amenities.get(listing.getId()));
        });

        return listings;
    }

    public List<Listing> findAllWithIds(List<Long> listingsIds) {
        if(listingsIds == null || listingsIds.isEmpty()) {
            return List.of();
        }

        List<Listing> listings = jdbcClient.sql(sqlRegistry.get("listing.findAllWithIds"))
                .param("ids", listingsIds)
                .query(this::mapRsToListing);

        HashMap<Long, List<Amenity>> amenities = this.findAllAmenitiesWithIds(listingsIds);

        listings.forEach(listing -> {
            listing.setAmenities(amenities.get(listing.getId()));
        });

        return listings;
    }

    public Listing findOneById(Long id) {
        Listing listing = jdbcClient.sql(sqlRegistry.get("listing.findOneById"))
                .param("id", id)
                .query(this::mapRsToListing)
                .stream().findFirst().orElseThrow(()-> new RuntimeException("Listing not found")); // Should throw custom exception but I'm lazy :p

        List<Amenity> amenities = jdbcClient.sql(sqlRegistry.get("listing.findAmenitiesByListingId"))
                .param("id", id)
                .query(this::mapAmenityRow);

        listing.setAmenities(amenities);
        return listing;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Listing save(ListingRequest listingRequest) {
        Long listingId = jdbcClient.sql(sqlRegistry.get("listing.save"))
                .params(this.prepareListingParams(listingRequest))
                .query((ResultSet rs, int rowMapper) -> rs.getLong("id"))
                .single();

        if (listingRequest.getAmenitiesIds() != null && !listingRequest.getAmenitiesIds().isEmpty()) {
            jdbcClient.sql(sqlRegistry.get("listing.addAmenities"))
                    .param("listing_id", listingId)
                    .param("amenity_ids", listingRequest.getAmenitiesIds().stream().map(Long::intValue).toArray(Integer[]::new))
                    .update();
        }

        return this.findOneById(listingId);
    }

    private HashMap<Long, List<Amenity>> findAllAmenitiesWithIds(List<Long> listingsIds) {
        return jdbcClient.sql(sqlRegistry.get("listing.findAmenitiesForListings"))
                .param("ids", listingsIds)
                .query(this::mapAmenityRowWithListingId);
    }

    private List<Listing> mapRsToListing(ResultSet rs) throws SQLException {
        List<Listing> listings = new ArrayList<>();
        while (rs.next()) {
            listings.add(new Listing(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("city"),
                    rs.getString("neighborhood"),
                    PropertyType.valueOf(rs.getString("type")),
                    PropertyStatus.valueOf(rs.getString("status")),
                    List.of(),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("area"),
                    rs.getInt("bedrooms"),
                    rs.getInt("bathrooms"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")
            ));
        }
        return listings;
    }

    private List<Amenity> mapAmenityRow(ResultSet rs) throws SQLException {
        List<Amenity> amenities = new ArrayList<>();
        while (rs.next()) {
            amenities.add(new Amenity(rs.getLong("id"), rs.getString("name")));
        }
        return amenities;
    }

    private HashMap<Long, List<Amenity>> mapAmenityRowWithListingId(ResultSet rs) throws SQLException {
        HashMap<Long, List<Amenity>> map = new HashMap<>();

        while (rs.next()) {
            long listingId = rs.getLong("listing_id");

            Amenity amenity = new Amenity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

            map.computeIfAbsent(listingId, k -> new ArrayList<>()).add(amenity);
        }

        return map;
    }

    private Map<String, Object> prepareListingParams(ListingRequest listing) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", listing.getTitle());
        params.put("description", listing.getDescription());
        params.put("city", listing.getCity());
        params.put("neighborhood", listing.getNeighborhood());
        params.put("type", listing.getType().name());
        params.put("status", listing.getStatus().name());
        params.put("price", listing.getPrice());
        params.put("area", listing.getArea());
        params.put("bedrooms", listing.getBedrooms());
        params.put("bathrooms", listing.getBathrooms());

        return params;
    }
}
