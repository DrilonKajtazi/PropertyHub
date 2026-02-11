package com.propertyhub.amenity.controller;

import com.propertyhub.amenity.entity.Amenity;
import com.propertyhub.amenity.service.AmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/amenity")
public class AmenityController {
    private final AmenityService amenityService;

    @PostMapping
    public Amenity save(@RequestBody Amenity amenity) {
        return this.amenityService.save(amenity);
    }
}
