package com.reduceco2now.ingestion;

import com.reduceco2now.catalog.FoodUpsert;

import java.util.List;

/** A product source that supplies one batch to an ingestion run. */
public interface IngestionSource {

    String sourceName();

    List<FoodUpsert> fetchBatch();
}
