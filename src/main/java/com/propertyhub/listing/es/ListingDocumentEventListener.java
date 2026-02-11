package com.propertyhub.listing.es;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class ListingDocumentEventListener {
    private final ListingDocumentService listingDocumentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSave(ListingSavedEvent listingSavedEvent) {
        listingDocumentService.upsert(ListingDoc.from(listingSavedEvent.listing()));
    }
}
