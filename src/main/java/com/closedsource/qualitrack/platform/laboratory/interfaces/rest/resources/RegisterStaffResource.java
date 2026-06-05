package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Register staff resource.
 */
@Schema(
        name = "RegisterStaffRequest",
        description = "Request payload for registering a new staff member",
        example = "{\"laboratoryId\": \"LAB-1234\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"role\": \"Quality Inspector\"}"
)
public record RegisterStaffResource(
        @Schema(description = "Target laboratory identifier", example = "LAB-1234")
        String laboratoryId,

        @Schema(description = "First name", example = "Jane", minLength = 1, maxLength = 50)
        String firstName,

        @Schema(description = "Last name", example = "Doe", minLength = 1, maxLength = 50)
        String lastName,

        @Schema(description = "Operational role", example = "Quality Inspector", minLength = 1, maxLength = 50)
        String role
) {
    /**
     * Validates the resource.
     * @throws IllegalArgumentException if any field is null or blank.
     */
    public RegisterStaffResource {
        if (laboratoryId == null || laboratoryId.isBlank()) throw new IllegalArgumentException("Laboratory ID is required");
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name is required");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name is required");
        if (role == null || role.isBlank()) throw new IllegalArgumentException("Role is required");
    }
}