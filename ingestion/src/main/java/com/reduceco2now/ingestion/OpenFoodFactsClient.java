package com.reduceco2now.ingestion;

import com.reduceco2now.catalog.FoodUpsert;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/** Open Food Facts ingestion source. */
public final class OpenFoodFactsClient implements IngestionSource {

    static final String SOURCE_NAME = "open-food-facts";

    private final OpenFoodFactsApi api;

    public OpenFoodFactsClient(OpenFoodFactsApi api) {
        this.api = Objects.requireNonNull(api);
    }

    @Override
    public String sourceName() {
        return SOURCE_NAME;
    }

    @Override
    public List<FoodUpsert> fetchBatch() {
        try {
            return List.copyOf(api.fetchBatch());
        } catch (OffApiException exception) {
            throw exception;
        } catch (IOException exception) {
            throw new OffApiException("Open Food Facts batch request failed", exception);
        }
    }
}
