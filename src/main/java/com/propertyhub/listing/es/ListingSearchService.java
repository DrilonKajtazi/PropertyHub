package com.propertyhub.listing.es;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.propertyhub.listing.model.PropertyStatus;
import com.propertyhub.listing.model.PropertyType;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListingSearchService {
    private final SearchOperations searchOperations;

    public List<Long> search(
            String text,
            String city,
            PropertyType type,
            PropertyStatus status,
            Double bedroomsMin,
            Double bedroomsMax,
            Double priceMin,
            Double priceMax,
            int limit,
            int offset
    ) {
        BoolQuery.Builder query = QueryBuilders.bool();

        if(StringUtils.isNotBlank(text)) {
            query.must(getTextQuery(List.of("title","description"),text));
        }
        if (city != null && !city.isBlank()) {
            query.filter(getTermQuery("city", city));
        }
        if (type != null) {
            query.filter(getTermQuery("type", type.name()));
        }
        if (status != null) {
            query.filter(getTermQuery("status", status.name()));
        }
        if (bedroomsMin != null || bedroomsMax != null) {
            query.filter(getRangeQuery("bedrooms", bedroomsMin, bedroomsMax));
        }
        if (priceMin != null || priceMax != null) {
            query.filter(getRangeQuery("price", priceMin, priceMax));
        }

        int page = offset / limit;
        NativeQuery nq = new NativeQueryBuilder()
                .withQuery(query.build()._toQuery())
                .withPageable(PageRequest.of(page, limit))
                .build();


        return searchOperations.search(nq, ListingDoc.class)
                .getSearchHits()
                .stream()
                .map(listingDocSearchHit ->  listingDocSearchHit.getContent().getId())
                .toList();
    }

    private static TermQuery getTermQuery(String fieldName, String value) {
        return QueryBuilders.term()
                .field(fieldName)
                .value(value)
                .build();
    }

    private static MultiMatchQuery getTextQuery(List<String> fieldsNames, String value) {
        return QueryBuilders.multiMatch()
                .fields(fieldsNames)
                .query(value)
                .fuzziness("AUTO")
                .build();
    }

    private static RangeQuery getRangeQuery(String fieldName, Double gte, Double lte) {
        return QueryBuilders.range()
                .number(
                        new NumberRangeQuery.Builder()
                                .gte(gte)
                                .lte(lte)
                                .field(fieldName)
                                .build()
                )
                .build();
    }
}
