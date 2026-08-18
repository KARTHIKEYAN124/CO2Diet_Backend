package com.reduceco2now.ingestion;

/** Batch-level Open Food Facts failure that must propagate to ingestion orchestration. */
public class OffApiException extends RuntimeException {

    public OffApiException(String message) {
        super(message);
    }

    public OffApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
