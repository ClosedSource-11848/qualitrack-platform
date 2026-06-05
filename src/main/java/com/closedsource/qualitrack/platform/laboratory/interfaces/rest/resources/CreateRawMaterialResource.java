package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Create raw material resource.
 */
@Schema(
        name = "CreateRawMaterialRequest",
        description = "Request payload for registering a new raw material with initial stock",
        example = "{\"laboratoryId\": \"LAB-1234\", \"name\": \"Distilled Water\", \"unit\": \"Liters\", \"initialStock\": 500}"
)
public record CreateRawMaterialResource(
        @Schema(description = "Target laboratory identifier", example = "LAB-1234")
        String laboratoryId,

        @Schema(description = "Raw material name", example = "Distilled Water", minLength = 1, maxLength = 100)
        String name,

        @Schema(description = "Measurement unit (e.g., Kg, Liters, Units)", example = "Liters", minLength = 1, maxLength = 20)
        String unit,

        @Schema(description = "Initial stock quantity", example = "500", minimum = "0")
        Integer initialStock
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if required fields are missing or invalid.
     */
    public CreateRawMaterialResource {
        if (laboratoryId == null || laboratoryId.isBlank()) throw new IllegalArgumentException("Laboratory ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (unit == null || unit.isBlank()) throw new IllegalArgumentException("Unit is required");
        if (initialStock == null || initialStock < 0) throw new IllegalArgumentException("Initial stock must be provided and cannot be negative");
    }
}