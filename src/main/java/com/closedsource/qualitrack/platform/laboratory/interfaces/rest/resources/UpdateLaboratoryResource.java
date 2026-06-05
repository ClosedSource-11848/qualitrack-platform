package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Update laboratory resource.
 */
@Schema(
        name = "UpdateLaboratoryRequest",
        description = "Request payload for updating an existing laboratory profile",
        example = "{\"name\": \"PharmaCorp Advanced\", \"regulationCode\": \"ISO-9001:2015\"}"
)
public record UpdateLaboratoryResource(
        @Schema(description = "Updated laboratory name", example = "PharmaCorp Advanced", minLength = 1, maxLength = 100)
        String name,

        @Schema(description = "Updated regulation code", example = "ISO-9001:2015", minLength = 1, maxLength = 50)
        String regulationCode
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if name or regulation code is null or blank.
     */
    public UpdateLaboratoryResource {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (regulationCode == null || regulationCode.isBlank()) throw new IllegalArgumentException("Regulation code is required");
    }
}