package com.example.catalogservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExplainAnalyzeRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ExplainAnalyzeRunner.class);
    private final JdbcTemplate jdbcTemplate;

    public ExplainAnalyzeRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("========================================");
        log.info("🚀 CHECKING GIN INDEX PERFORMANCE...");
        log.info("========================================");

        try {
            // Check total watches
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM watches", Integer.class);
            log.info("Total watches in DB: {}", count);

            // The JSONB Query
            String query = "EXPLAIN ANALYZE SELECT * FROM watches WHERE specifications @> '{\"glass\": \"Sapphire\"}'::jsonb";
            log.info("Executing: {}", query);
            
            List<String> results = jdbcTemplate.query(query, (rs, rowNum) -> rs.getString(1));
            
            boolean usesGinIndex = false;
            log.info("--- EXPLAIN ANALYZE OUTPUT ---");
            for (String line : results) {
                log.info(line);
                if (line.contains("Bitmap Index Scan on idx_watches_specs")) {
                    usesGinIndex = true;
                }
            }
            log.info("------------------------------");
            
            if (usesGinIndex) {
                log.info("✅ SUCCESS: GIN Index (idx_watches_specs) is being used correctly!");
            } else {
                log.info("⚠️ WARNING: GIN Index is NOT used. Might be falling back to Seq Scan due to small table size.");
            }
        } catch (Exception e) {
            log.error("Failed to run EXPLAIN ANALYZE: {}", e.getMessage());
        }
        log.info("========================================");
    }
}
