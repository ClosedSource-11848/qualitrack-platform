package com.closedsource.qualitrack.platform.laboratory.interfaces.rest;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.StaffCommandService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.DeactivateStaffCommand;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.resources.MessageResource;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes direct operations on staff members.
 */
@RestController
@RequestMapping(value = "/api/v1/staff", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Staff", description = "Staff management endpoints") // ¡Etiqueta actualizada e independiente!
public class StaffController {

    private final StaffCommandService staffCommandService;

    public StaffController(StaffCommandService staffCommandService) {
        this.staffCommandService = staffCommandService;
    }

    @PutMapping("/{staffId}/deactivation")
    @Operation(summary = "Deactivate staff member", description = "Deactivates an employee's access across the platform.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff deactivated successfully",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Staff not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Business rule violation",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> deactivateStaff(
            @PathVariable @Parameter(description = "Staff numeric identifier", example = "99", required = true) Long staffId
    ) {
        var command = new DeactivateStaffCommand(staffId);

        var result = staffCommandService.handle(command)
                .map(id -> new MessageResource("Staff member deactivated successfully"));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }
}