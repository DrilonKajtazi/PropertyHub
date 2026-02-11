package com.propertyhub.dev.seed;

import com.propertyhub.listing.entity.Listing;
import com.propertyhub.listing.model.ListingRequest;
import com.propertyhub.listing.model.PropertyStatus;
import com.propertyhub.listing.model.PropertyType;
import com.propertyhub.listing.service.ListingService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DevDataSeeder implements ApplicationRunner {

    private final JdbcTemplate jdbc;
    private final ListingService listingService;

    @Override
    public void run(ApplicationArguments args) {
        Integer count = jdbc.queryForObject("select count(*) from listings", Integer.class);
        if (count != null && count > 2000) return;

        seedAmenities();

        var faker = new Faker();
        var cities = List.of("Prishtina", "Ferizaj", "Prizren", "Peja", "Gjilan");
        var neighborhoods = Map.of(
                "Prishtina", List.of("Bregu i Diellit", "Ulpiana", "Dardania", "Arbëria"),
                "Ferizaj", List.of("Qendër", "Varosh", "Taslixhe"),
                "Prizren", List.of("Bazhdarhane", "Ortakoll", "Tabakhane"),
                "Peja", List.of("Kapeshnicë", "Karagaç"),
                "Gjilan", List.of("Qendër", "Dheu i Bardhë")
        );

        List<Long> amenityIds = jdbc.queryForList("select id from amenity order by id", Long.class);

        int n = 2000; // change
        for (int i = 0; i < n; i++) {
            String city = cities.get(faker.random().nextInt(cities.size()));
            String hood = neighborhoods.get(city).get(faker.random().nextInt(neighborhoods.get(city).size()));

            PropertyType type = faker.options().option(PropertyType.values());
            PropertyStatus status = faker.options().option(PropertyStatus.values());

            Integer bedrooms = (type == PropertyType.LAND) ? null : faker.number().numberBetween(1, 5);
            Integer bathrooms = (type == PropertyType.LAND) ? null : faker.number().numberBetween(1, 3);

            BigDecimal area = (switch (type) {
                case APARTMENT -> BigDecimal.valueOf(faker.number().numberBetween(45, 160));
                case HOUSE -> BigDecimal.valueOf(faker.number().numberBetween(90, 320));
                case LAND -> BigDecimal.valueOf(faker.number().numberBetween(200, 2000));
            }).setScale(2, java.math.RoundingMode.HALF_UP);

            BigDecimal price = realisticPrice(city, type, area).setScale(2);

            String title = titleTemplate(type, bedrooms, city, hood);
            String description = descriptionTemplate(type, bedrooms, bathrooms, city, hood);

            ListingRequest listingRequest = new ListingRequest(title, description, city, hood, type,
                    status, amenityIds, price, area, bedrooms, bathrooms);
            listingService.save(listingRequest);
        }
    }

    private void seedAmenities() {
        Integer c = jdbc.queryForObject("select count(*) from amenity", Integer.class);
        if (c != null && c > 0) return;

        var names = List.of("PARKING", "BALCONY", "ELEVATOR", "GARDEN", "AIR_CONDITIONING", "FURNISHED", "STORAGE", "SECURITY");
        jdbc.batchUpdate("insert into amenity(name) values (?) on conflict do nothing",
                names, 100, (ps, name) -> ps.setString(1, name));
    }

    private static BigDecimal realisticPrice(String city, PropertyType type, BigDecimal area) {
        // simple heuristic: price = area * pricePerM2 (+ noise), different by city/type
        BigDecimal base = switch (city) {
            case "Prishtina" -> BigDecimal.valueOf(1200);
            case "Prizren" -> BigDecimal.valueOf(900);
            default -> BigDecimal.valueOf(750);
        };
        BigDecimal typeMult = switch (type) {
            case APARTMENT -> BigDecimal.valueOf(1.00);
            case HOUSE -> BigDecimal.valueOf(1.15);
            case LAND -> BigDecimal.valueOf(0.55);
        };
        BigDecimal perM2 = base.multiply(typeMult);
        return area.multiply(perM2);
    }

    private static String titleTemplate(PropertyType type, Integer bedrooms, String city, String hood) {
        return switch (type) {
            case APARTMENT -> "%d-bedroom apartment in %s (%s)".formatted(bedrooms, city, hood);
            case HOUSE -> "Family house with %d bedrooms in %s (%s)".formatted(bedrooms, city, hood);
            case LAND -> "Land plot in %s (%s)".formatted(city, hood);
        };
    }

    private static String descriptionTemplate(PropertyType type, Integer bedrooms, Integer bathrooms, String city, String hood) {
        return switch (type) {
            case APARTMENT ->
                    "Bright apartment in %s, %s. %d bedrooms, %d bathrooms. Close to shops and public transport."
                            .formatted(city, hood, bedrooms, bathrooms);
            case HOUSE ->
                    "Well-kept house in %s, %s with %d bedrooms and %d bathrooms. Quiet street, ideal for families."
                            .formatted(city, hood, bedrooms, bathrooms);
            case LAND -> "Land in %s, %s suitable for residential build. Easy road access."
                    .formatted(city, hood);
        };
    }
}
