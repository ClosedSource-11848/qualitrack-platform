package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Laboratory resource.
 */
@Schema(
        name = "LaboratoryResponse",
        description = "Laboratory information response",
        example = "{\"id\": \"LAB-1234\", \"name\": \"PharmaCorp\", \"regulationCode\": \"ISO-9001\", \"status\": \"ACTIVE\", \"street\": \"123 Tech Ave\", \"city\": \"Lima\", \"zipCode\": \"15001\"}"
)
public record LaboratoryResource(
        @Schema(description = "Laboratory unique identifier", example = "LAB-1234")
        String id,

        @Schema(description = "Laboratory official name", example = "PharmaCorp")
        String name,

        @Schema(description = "Current regulatory compliance code", example = "ISO-9001")
        String regulationCode,

        @Schema(description = "Current operational status", example = "ACTIVE")
        String status,

        @Schema(description = "Street address", example = "123 Tech Ave")
        String street,

        @Schema(description = "City location", example = "Lima")
        String city,

        @Schema(description = "Postal or zip code", example = "15001")
        String zipCode
) {
}