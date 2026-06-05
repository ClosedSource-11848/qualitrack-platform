package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Pharmaceutical product resource.
 */
@Schema(
        name = "PharmaceuticalProductResponse",
        description = "Pharmaceutical product information response",
        example = "{\"id\": \"PROD-001\", \"laboratoryId\": \"LAB-1234\", \"name\": \"Aspirin 500mg\", \"description\": \"Pain reliever and fever reducer\", \"activeIngredient\": \"Acetylsalicylic acid\"}"
)
public record PharmaceuticalProductResource(
        @Schema(description = "Product unique identifier", example = "PROD-001")
        String id,

        @Schema(description = "Associated laboratory identifier", example = "LAB-1234")
        String laboratoryId,

        @Schema(description = "Product name", example = "Aspirin 500mg")
        String name,

        @Schema(description = "Detailed product description", example = "Pain reliever and fever reducer")
        String description,

        @Schema(description = "Primary active ingredient", example = "Acetylsalicylic acid")
        String activeIngredient
) {
}