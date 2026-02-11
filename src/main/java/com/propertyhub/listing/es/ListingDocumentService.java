package com.propertyhub.listing.es;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.DocumentOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ListingDocumentService {
    private final DocumentOperations documentOperations;

    public void upsert(ListingDoc listingDoc) {
        documentOperations.save(listingDoc);
    }

    public void delete(String id) {
        // Check if it's better to use .delete(Entity entity);
        documentOperations.delete(id, ListingDoc.class);
    }
}
