package com.reduceco2now.ingestion;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OpenFoodFactsClientTest {

    @Test
    void returnsEmptyBatchWhenApiRequestSucceedsWithNoProducts() {
        OpenFoodFactsClient client = new OpenFoodFactsClient(List::of);

        assertThat(client.fetchBatch()).isEmpty();
    }

    @Test
    void rethrowsBatchLevelApiFailure() {
        OffApiException failure = new OffApiException("OFF unavailable");
        OpenFoodFactsClient client = new OpenFoodFactsClient(() -> {
            throw failure;
        });

        assertThatThrownBy(client::fetchBatch).isSameAs(failure);
    }

    @Test
    void translatesTransportFailureToBatchLevelApiFailure() {
        IOException failure = new IOException("connection reset");
        OpenFoodFactsClient client = new OpenFoodFactsClient(() -> {
            throw failure;
        });

        assertThatThrownBy(client::fetchBatch)
                .isInstanceOf(OffApiException.class)
                .hasMessage("Open Food Facts batch request failed")
                .hasCause(failure);
    }
}
