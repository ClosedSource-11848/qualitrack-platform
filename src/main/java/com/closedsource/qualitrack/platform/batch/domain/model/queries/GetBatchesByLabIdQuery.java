package com.closedsource.qualitrack.platform.batch.domain.model.queries;

/**
 * Query to get all production batches belonging to a specific laboratory.
 *
 * @param laboratoryId The ID of the laboratory. Cannot be null or less than 1.
 */
public record GetBatchesByLabIdQuery(Long laboratoryId) {
    public GetBatchesByLabIdQuery {
        if (laboratoryId == null || laboratoryId <= 0) {
            throw new IllegalArgumentException("Laboratory id is required and must be greater than 0.");
        }
    }
}