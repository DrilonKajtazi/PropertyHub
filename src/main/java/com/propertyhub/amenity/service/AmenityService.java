package com.propertyhub.amenity.service;

import com.propertyhub.amenity.entity.Amenity;
import com.propertyhub.amenity.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AmenityService {
    private final AmenityRepository amenityRepository;

    public Amenity save(Amenity amenity){
         return this.amenityRepository.save(amenity.getName());
    }

    public Amenity findOneById(Long id){
        return this.amenityRepository.findOneById(id);
    }
}
