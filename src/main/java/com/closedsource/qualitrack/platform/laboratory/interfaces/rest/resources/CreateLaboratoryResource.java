package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Create laboratory resource.
 */
@Schema(
        name = "CreateLaboratoryRequest",
        description = "Request payload for creating a new laboratory",
        example = "{\"name\": \"PharmaCorp\", \"regulationCode\": \"ISO-9001\", \"street\": \"123 Tech Ave\", \"city\": \"Lima\", \"zipCode\": \"15001\"}"
)
public record CreateLaboratoryResource(
        @Schema(description = "Laboratory official name", example = "PharmaCorp", minLength = 1, maxLength = 100)
        String name,

        @Schema(description = "Regulatory compliance code", example = "ISO-9001", minLength = 1, maxLength = 50)
        String regulationCode,

        @Schema(description = "Street address", example = "123 Tech Ave", minLength = 1, maxLength = 150)
        String street,

        @Schema(description = "City location", example = "Lima", minLength = 1, maxLength = 100)
        String city,

        @Schema(description = "Postal or zip code", example = "15001", minLength = 1, maxLength = 20)
        String zipCode
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any required field is null or blank.
     */
    public CreateLaboratoryResource {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (regulationCode == null || regulationCode.isBlank()) throw new IllegalArgumentException("Regulation code is required");
        if (street == null || street.isBlank()) throw new IllegalArgumentException("Street is required");
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City is required");
        if (zipCode == null || zipCode.isBlank()) throw new IllegalArgumentException("Zip code is required");
    }
}