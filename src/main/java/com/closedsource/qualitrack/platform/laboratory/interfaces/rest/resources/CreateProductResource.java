package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Create product resource.
 */
@Schema(
        name = "CreateProductRequest",
        description = "Request payload for creating a new pharmaceutical product",
        example = "{\"laboratoryId\": \"LAB-1234\", \"name\": \"Aspirin 500mg\", \"description\": \"Pain reliever\", \"activeIngredient\": \"Acetylsalicylic acid\"}"
)
public record CreateProductResource(
        @Schema(description = "Target laboratory identifier", example = "LAB-1234")
        String laboratoryId,

        @Schema(description = "Product name", example = "Aspirin 500mg", minLength = 1, maxLength = 100)
        String name,

        @Schema(description = "Detailed product description", example = "Pain reliever", maxLength = 500)
        String description,

        @Schema(description = "Primary active ingredient", example = "Acetylsalicylic acid", minLength = 1, maxLength = 100)
        String activeIngredient
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if required fields are missing or invalid.
     */
    public CreateProductResource {
        if (laboratoryId == null || laboratoryId.isBlank()) throw new IllegalArgumentException("Laboratory ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name is required");
        if (activeIngredient == null || activeIngredient.isBlank()) throw new IllegalArgumentException("Active ingredient is required");
    }
}