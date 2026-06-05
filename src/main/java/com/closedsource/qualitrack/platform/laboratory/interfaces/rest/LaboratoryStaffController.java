package com.closedsource.qualitrack.platform.laboratory.interfaces.rest;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.StaffCommandService;
import com.closedsource.qualitrack.platform.laboratory.application.queryservices.StaffQueryService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.DeactivateStaffCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.RegisterStaffCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.queries.GetStaffByLabIdQuery;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.RegisterStaffResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.StaffMemberResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform.StaffResourceFromEntityAssembler;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes staff member resources for a specific laboratory.
 */
@RestController
@RequestMapping(value = "/api/v1/laboratories/{laboratoryId}/staff", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Laboratory Staff", description = "Laboratory staff management endpoints")
public class LaboratoryStaffController {
    private final StaffCommandService staffCommandService;
    private final StaffQueryService staffQueryService;

    public LaboratoryStaffController(StaffCommandService staffCommandService, StaffQueryService staffQueryService) {
        this.staffCommandService = staffCommandService;
        this.staffQueryService = staffQueryService;
    }

    @PostMapping
    @Operation(summary = "Register new staff member", description = "Registers an employee to a specific laboratory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Staff registered successfully", content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Laboratory not found")
    })
    public ResponseEntity<?> registerStaff(
            @PathVariable @Parameter(description = "Laboratory identifier", required = true) String laboratoryId,
            @RequestBody RegisterStaffResource resource
    ) {
        // Ignoramos el laboratoryId del body por seguridad y usamos el del PathVariable
        var command = new RegisterStaffCommand(laboratoryId, resource.firstName(), resource.lastName(), resource.role());
        var result = staffCommandService.handle(command)
                .map(staffId -> new MessageResource("Staff member registered successfully with ID: " + staffId));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get laboratory staff", description = "Retrieves all staff members belonging to a specific laboratory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff retrieved successfully")
    })
    public ResponseEntity<List<StaffMemberResource>> getStaffByLaboratoryId(
            @PathVariable @Parameter(description = "Laboratory identifier", required = true) String laboratoryId
    ) {
        var staff = staffQueryService.handle(new GetStaffByLabIdQuery(laboratoryId));
        var resources = staff.stream().map(StaffResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{staffId}/deactivation")
    @Operation(summary = "Deactivate staff member", description = "Deactivates an employee's access to the laboratory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Staff or laboratory not found"),
            @ApiResponse(responseCode = "409", description = "Business rule violation")
    })
    public ResponseEntity<?> deactivateStaff(
            @PathVariable @Parameter(description = "Laboratory identifier", required = true) String laboratoryId,
            @PathVariable @Parameter(description = "Staff identifier", required = true) String staffId
    ) {
        var command = new DeactivateStaffCommand(staffId, laboratoryId);
        var result = staffCommandService.handle(command)
                .map(id -> new MessageResource("Staff member deactivated successfully"));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }
}