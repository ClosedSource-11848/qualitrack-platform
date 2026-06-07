package com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Register maintenance resource.
 */
@Schema(
        name = "RegisterMaintenanceRequest",
        description = "Request payload for logging a new maintenance intervention",
        example = "{\"equipmentId\": 1, \"maintenanceDate\": \"2026-05-20\", \"technicianName\": \"John Smith\", \"description\": \"Annual calibration and sensor replacement\", \"type\": \"PREVENTIVE\"}"
)
public record RegisterMaintenanceResource(

        @Schema(description = "Target equipment numeric identifier", example = "1")
        Long equipmentId,

        @Schema(description = "Date the maintenance was performed (ISO 8601)", example = "2026-05-20")
        String maintenanceDate,

        @Schema(description = "Name of the technician", example = "John Smith", minLength = 1, maxLength = 150)
        String technicianName,

        @Schema(description = "Detailed description of the work done", example = "Annual calibration and sensor replacement", minLength = 1, maxLength = 1000)
        String description,

        @Schema(description = "Type of maintenance (e.g., PREVENTIVE, CORRECTIVE)", example = "PREVENTIVE", minLength = 1, maxLength = 50)
        String type
) {
    /**
     * Validates the resource properties (Fail-Fast).
     * @throws IllegalArgumentException if required fields are missing or invalid.
     */
    public RegisterMaintenanceResource {
        if (equipmentId == null || equipmentId <= 0) {
            throw new IllegalArgumentException("Equipment ID is required and must be greater than zero");
        }
        if (maintenanceDate == null || maintenanceDate.isBlank()) {
            throw new IllegalArgumentException("Maintenance date is required");
        }
        if (technicianName == null || technicianName.isBlank()) {
            throw new IllegalArgumentException("Technician name is required");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Maintenance type is required");
        }
    }
}