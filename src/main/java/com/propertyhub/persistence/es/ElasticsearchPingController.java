package com.propertyhub.persistence.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElasticsearchPingController {

    private final ElasticsearchClient es;

    public ElasticsearchPingController(ElasticsearchClient es) {
        this.es = es;
    }

    @GetMapping("/api/es/info")
    public Object info() throws Exception {
        return es.info();
    }
}
