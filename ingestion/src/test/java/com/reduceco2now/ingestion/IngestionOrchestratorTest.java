package com.reduceco2now.ingestion;

import com.reduceco2now.catalog.CatalogCommand;
import com.reduceco2now.catalog.FoodUpsert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IngestionOrchestratorTest {

    @Test
    void reportsSuccessfulEmptyBatchAsSuccess() {
        IngestionOrchestrator orchestrator = new IngestionOrchestrator(
                List.of(new OpenFoodFactsClient(List::of)),
                upsert -> null
        );

        assertThat(orchestrator.runBatch()).containsExactly(
                new SourceIngestionResult(
                        OpenFoodFactsClient.SOURCE_NAME,
                        SourceIngestionResult.Status.SUCCESS,
                        0,
                        null
                )
        );
    }

    @Test
    void reportsFailedSourceAndContinuesWithRemainingSources() {
        IngestionSource failedOffSource = new OpenFoodFactsClient(() -> {
            throw new OffApiException("OFF unavailable");
        });
        FoodUpsert product = new FoodUpsert("123", "Apple", "Farm", "fruit");
        IngestionSource succeedingSource = new TestSource("usda", List.of(product));
        List<FoodUpsert> upserts = new ArrayList<>();
        CatalogCommand catalog = upsert -> {
            upserts.add(upsert);
            return null;
        };
        IngestionOrchestrator orchestrator = new IngestionOrchestrator(
                List.of(failedOffSource, succeedingSource),
                catalog
        );

        assertThat(orchestrator.runBatch()).containsExactly(
                new SourceIngestionResult(
                        OpenFoodFactsClient.SOURCE_NAME,
                        SourceIngestionResult.Status.FAILED,
                        0,
                        "OFF unavailable"
                ),
                new SourceIngestionResult("usda", SourceIngestionResult.Status.SUCCESS, 1, null)
        );
        assertThat(upserts).containsExactly(product);
    }

    private record TestSource(String sourceName, List<FoodUpsert> products) implements IngestionSource {
        @Override
        public List<FoodUpsert> fetchBatch() {
            return products;
        }
    }
}
