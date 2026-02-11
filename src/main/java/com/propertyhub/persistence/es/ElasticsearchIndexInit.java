package com.propertyhub.persistence.es;

import com.propertyhub.listing.es.ListingDoc;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ElasticsearchIndexInit {
    private final ElasticsearchOperations ops;

    @PostConstruct
    private void init(){
        IndexOperations indexOps = ops.indexOps(ListingDoc.class);

        IndexCoordinates coords = indexOps.getIndexCoordinates();
        String[] indexNames = coords.getIndexNames(); // String[]

        if (!indexOps.exists()) {
            indexOps.createWithMapping();
            log.info("Created Elasticsearch index: {}", Arrays.toString(indexNames));
        } else {
            log.info("Elasticsearch index already exists: {}", Arrays.toString(indexNames));
        }
    }
}
