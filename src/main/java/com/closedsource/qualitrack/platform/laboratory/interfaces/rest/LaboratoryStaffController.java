package com.closedsource.qualitrack.platform.laboratory.interfaces.rest;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.StaffCommandService;
import com.closedsource.qualitrack.platform.laboratory.application.queryservices.StaffQueryService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.RegisterStaffCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.queries.GetStaffByLabIdQuery;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.RegisterStaffResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.StaffMemberResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform.RegisterStaffCommandFromResourceAssembler;
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
@Tag(name = "Laboratories", description = "Laboratory management endpoints")
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
            @PathVariable @Parameter(description = "Laboratory numeric identifier", example = "1", required = true) Long laboratoryId,
            @RequestBody RegisterStaffResource resource
    ) {
        var command = RegisterStaffCommandFromResourceAssembler.toCommandFromResource(resource);

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
            @PathVariable @Parameter(description = "Laboratory numeric identifier", example = "1", required = true) Long laboratoryId
    ) {
        var staff = staffQueryService.handle(new GetStaffByLabIdQuery(laboratoryId));
        var resources = staff.stream().map(StaffResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}