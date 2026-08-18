package com.reduceco2now.ingestion;

/** Outcome for one source in an ingestion run. */
public record SourceIngestionResult(
        String source,
        Status status,
        int fetchedProducts,
        String failureMessage
) {
    public enum Status {
        SUCCESS,
        FAILED
    }

    static SourceIngestionResult success(String source, int fetchedProducts) {
        return new SourceIngestionResult(source, Status.SUCCESS, fetchedProducts, null);
    }

    static SourceIngestionResult failed(String source, String failureMessage) {
        return new SourceIngestionResult(source, Status.FAILED, 0, failureMessage);
    }
}
