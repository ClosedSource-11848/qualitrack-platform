package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Raw material resource.
 */
@Schema(
        name = "RawMaterialResponse",
        description = "Raw material information response",
        example = "{\"id\": \"RM-777\", \"laboratoryId\": \"LAB-1234\", \"name\": \"Distilled Water\", \"unit\": \"Liters\", \"currentStock\": 500}"
)
public record RawMaterialResource(
        @Schema(description = "Raw material unique identifier", example = "RM-777")
        String id,

        @Schema(description = "Associated laboratory identifier", example = "LAB-1234")
        String laboratoryId,

        @Schema(description = "Raw material name", example = "Distilled Water")
        String name,

        @Schema(description = "Measurement unit", example = "Liters")
        String unit,

        @Schema(description = "Current available stock quantity", example = "500")
        Integer currentStock
) {
}