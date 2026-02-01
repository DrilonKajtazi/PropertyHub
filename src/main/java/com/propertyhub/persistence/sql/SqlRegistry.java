package com.propertyhub.persistence.sql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SqlRegistry {

    private final Map<String, String> queries = new ConcurrentHashMap<>();

    // Only scan your app packages, and only folders named "sql"
    @Value("${app.sql.scan-pattern}")
    private String scanPattern;

    private static final Pattern NAMESPACE_PATTERN =
            Pattern.compile(".*/([^/]+)/sql/[^/]+\\.sql.*"); // last folder before /sql/

    @PostConstruct
    public void loadAll() throws IOException {
        var resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(scanPattern);

        for (Resource r : resources) {
            String namespace = inferNamespace(r);
            String content = new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            parseNamedQueries(content, namespace, r.getDescription());
        }

        if (queries.isEmpty()) {
            throw new IllegalStateException("No SQL queries loaded. Pattern=" + scanPattern);
        }
    }

    public String get(String key) {
        String sql = queries.get(key);
        if (sql == null) {
            throw new IllegalArgumentException("SQL query not found: " + key);
        }
        return sql;
    }

    private String inferNamespace(Resource r) throws IOException {
        // Works for both exploded classes and jar: URLs
        String desc = r.getURL().toString();
        Matcher m = NAMESPACE_PATTERN.matcher(desc);
        if (!m.matches()) {
            throw new IllegalStateException("Cannot infer namespace from: " + desc);
        }
        return m.group(1); // e.g. "user" or "listing"
    }

    private void parseNamedQueries(String fileContent, String namespace, String source) {
        String[] lines = fileContent.split("\\R");
        String currentName = null;
        StringBuilder buf = new StringBuilder();

        for (String raw : lines) {
            String line = raw.trim();
            if (line.startsWith("-- name:")) {
                // flush previous
                flush(namespace, currentName, buf, source);
                currentName = line.substring("-- name:".length()).trim();
                buf.setLength(0);
            } else {
                if (currentName != null) buf.append(raw).append('\n');
            }
        }
        flush(namespace, currentName, buf, source);
    }

    private void flush(String namespace, String name, StringBuilder buf, String source) {
        if (name == null) return;
        String sql = buf.toString().trim();
        if (sql.isEmpty()) return;

        // If you wrote "user.findByUsername" explicitly, keep it.
        // Otherwise, auto-prefix with namespace: "user.findByUsername"
        String key = name.contains(".") ? name : (namespace + "." + name);

        String prev = queries.putIfAbsent(key, sql);
        if (prev != null) {
            throw new IllegalStateException("Duplicate SQL key '" + key + "' from " + source);
        }
    }
}

