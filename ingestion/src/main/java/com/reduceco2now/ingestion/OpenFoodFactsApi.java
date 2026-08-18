package com.reduceco2now.ingestion;

import com.reduceco2now.catalog.FoodUpsert;

import java.io.IOException;
import java.util.List;

/** Low-level Open Food Facts API access used by the source client. */
@FunctionalInterface
public interface OpenFoodFactsApi {

    List<FoodUpsert> fetchBatch() throws IOException;
}
