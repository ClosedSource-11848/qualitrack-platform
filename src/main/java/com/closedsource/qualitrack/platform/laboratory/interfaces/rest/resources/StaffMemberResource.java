package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Staff member resource.
 */
@Schema(
        name = "StaffMemberResponse",
        description = "Staff member information response",
        example = "{\"id\": \"EMP-999\", \"laboratoryId\": \"LAB-1234\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"role\": \"Quality Inspector\", \"active\": true}"
)
public record StaffMemberResource(
        @Schema(description = "Staff member unique identifier", example = "EMP-999")
        String id,

        @Schema(description = "Associated laboratory identifier", example = "LAB-1234")
        String laboratoryId,

        @Schema(description = "First name", example = "Jane")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @Schema(description = "Assigned operational role", example = "Quality Inspector")
        String role,

        @Schema(description = "Is the staff member currently active", example = "true")
        boolean active
) {
}