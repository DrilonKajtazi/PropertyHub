package com.propertyhub.listing.controller;

import com.propertyhub.listing.entity.Listing;
import com.propertyhub.listing.model.ListingRequest;
import com.propertyhub.listing.model.ListingResponse;
import com.propertyhub.listing.model.PropertyStatus;
import com.propertyhub.listing.model.PropertyType;
import com.propertyhub.listing.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/listing")
public class ListingController {
    private final ListingService listingService;

    @GetMapping("/{id}")
    public ListingResponse findOneById(@PathVariable Long id){
        return this.listingService.findOneById(id);
    }

    @GetMapping
    public List<ListingResponse> findAll(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) PropertyType type,
            @RequestParam(required = false) PropertyStatus status,
            @RequestParam(required = false) Double bedroomsMin,
            @RequestParam(required = false) Double bedroomsMax,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") int offset
            ){
        return this.listingService.findAll(text, city, type, status, bedroomsMin, bedroomsMax, priceMin, priceMax, limit, offset);
    }

    @PostMapping
    public ListingResponse save(@RequestBody ListingRequest listing){
        return this.listingService.save(listing);
    }
}
