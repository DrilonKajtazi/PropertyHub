package com.propertyhub.listing.service;

import com.propertyhub.listing.entity.Listing;
import com.propertyhub.listing.es.ListingSavedEvent;
import com.propertyhub.listing.es.ListingSearchService;
import com.propertyhub.listing.model.ListingRequest;
import com.propertyhub.listing.model.ListingResponse;
import com.propertyhub.listing.model.PropertyStatus;
import com.propertyhub.listing.model.PropertyType;
import com.propertyhub.listing.repository.ListingRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListingService {
    private final ListingRepository listingRepository;
    private final ApplicationEventPublisher publisher;
    private final ListingSearchService listingSearchService;

    public List<ListingResponse> findAll(
            String text,
            String city,
            PropertyType type,
            PropertyStatus status,
            Double bedroomsMin,
            Double bedroomsMax,
            Double priceMin,
            Double priceMax,
            int limit,
            int offset) {
        if (StringUtils.isNotBlank(text)) {
            List<Long> listingsIds = this.listingSearchService.search(text, city, type, status, bedroomsMin, bedroomsMax, priceMin, priceMax, limit, offset);
            return this.listingRepository.findAllWithIds(listingsIds).stream().map(ListingResponse::from).toList();
        }
        return this.listingRepository.findAll(limit, offset).stream().map(ListingResponse::from).toList();
    }

    public ListingResponse findOneById(long id) {
        return ListingResponse.from(listingRepository.findOneById(id));
    }

    @Transactional
    public ListingResponse save(ListingRequest listing) {
        Listing listingEntity = this.listingRepository.save(listing);
        publisher.publishEvent(new ListingSavedEvent(listingEntity));
        return ListingResponse.from(listingEntity);
    }
}
