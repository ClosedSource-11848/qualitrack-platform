package com.closedsource.qualitrack.platform.laboratory.domain.model.queries;

/**
 * Query to get laboratory by id.
 *
 * @param laboratoryId The ID of the laboratory to retrieve. Cannot be null or blank.
 */
public record GetLaboratoryByIdQuery(String laboratoryId) {
    /**
     * Compact constructor for GetLaboratoryByIdQuery.
     * Validates that the laboratoryId is not null and is not blank.
     * @throws IllegalArgumentException if laboratoryId is null or blank.
     */
    public GetLaboratoryByIdQuery {
        if (laboratoryId == null || laboratoryId.isBlank()) {
            throw new IllegalArgumentException("Laboratory id is required.");
        }
    }
}