package com.reduceco2now.ingestion;

import com.reduceco2now.catalog.CatalogCommand;
import com.reduceco2now.catalog.FoodUpsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Runs each ingestion source independently and reports source-level failures. */
public final class IngestionOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(IngestionOrchestrator.class);

    private final List<IngestionSource> sources;
    private final CatalogCommand catalog;

    public IngestionOrchestrator(List<IngestionSource> sources, CatalogCommand catalog) {
        this.sources = List.copyOf(sources);
        this.catalog = Objects.requireNonNull(catalog);
    }

    public List<SourceIngestionResult> runBatch() {
        List<SourceIngestionResult> results = new ArrayList<>();
        for (IngestionSource source : sources) {
            try {
                List<FoodUpsert> products = source.fetchBatch();
                products.forEach(catalog::upsert);
                results.add(SourceIngestionResult.success(source.sourceName(), products.size()));
            } catch (OffApiException exception) {
                log.error("Ingestion source {} failed", source.sourceName(), exception);
                results.add(SourceIngestionResult.failed(source.sourceName(), exception.getMessage()));
            }
        }
        return List.copyOf(results);
    }
}
