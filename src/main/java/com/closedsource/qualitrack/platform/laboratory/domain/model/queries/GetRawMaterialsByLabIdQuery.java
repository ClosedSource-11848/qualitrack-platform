package com.closedsource.qualitrack.platform.laboratory.domain.model.queries;

/**
 * Query to get all raw materials by laboratory id.
 *
 * @param laboratoryId The ID of the laboratory. Cannot be null or blank.
 */
public record GetRawMaterialsByLabIdQuery(String laboratoryId) {
    /**
     * Compact constructor for GetRawMaterialsByLabIdQuery.
     * Validates that the laboratoryId is not null and is not blank.
     * @throws IllegalArgumentException if laboratoryId is null or blank.
     */
    public GetRawMaterialsByLabIdQuery {
        if (laboratoryId == null || laboratoryId.isBlank()) {
            throw new IllegalArgumentException("Laboratory id is required.");
        }
    }
}