package com.closedsource.qualitrack.platform.equipment.interfaces.rest;

import com.closedsource.qualitrack.platform.equipment.application.commandservices.EquipmentCommandService;
import com.closedsource.qualitrack.platform.equipment.application.queryservices.EquipmentQueryService;
import com.closedsource.qualitrack.platform.equipment.domain.model.aggregates.Equipment;
import com.closedsource.qualitrack.platform.equipment.domain.model.queries.GetEquipmentByIdQuery;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.EquipmentResource;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.RegisterEquipmentResource;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.transform.EquipmentResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.transform.RegisterEquipmentCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
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
 * REST controller that exposes equipment resources and administration endpoints.
 */
@RestController
@RequestMapping(value = "/api/v1/equipments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Equipment", description = "Equipment management endpoints")
public class EquipmentController {

    private final EquipmentCommandService equipmentCommandService;
    private final EquipmentQueryService equipmentQueryService;

    public EquipmentController(EquipmentCommandService equipmentCommandService, EquipmentQueryService equipmentQueryService) {
        this.equipmentCommandService = equipmentCommandService;
        this.equipmentQueryService = equipmentQueryService;
    }

    @PostMapping
    @Operation(summary = "Register new equipment", description = "Registers a new equipment and assigns it to a laboratory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipment registered successfully", content = @Content(schema = @Schema(implementation = EquipmentResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Target laboratory not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - Equipment serial number already exists")
    })
    public ResponseEntity<?> registerEquipment(@RequestBody RegisterEquipmentResource resource) {
        var command = RegisterEquipmentCommandFromResourceAssembler.toCommandFromResource(resource);

        var result = equipmentCommandService.handle(command)
                .flatMap(equipmentId -> equipmentQueryService.handle(new GetEquipmentByIdQuery(equipmentId))
                        .<Result<Equipment, ApplicationError>>map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("Equipment", equipmentId))));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                EquipmentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{equipmentId}")
    @Operation(summary = "Get equipment by ID", description = "Retrieves a specific equipment by its numeric identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment found", content = @Content(schema = @Schema(implementation = EquipmentResource.class))),
            @ApiResponse(responseCode = "404", description = "Equipment not found")
    })
    public ResponseEntity<EquipmentResource> getEquipmentById(
            @PathVariable @Parameter(description = "Equipment numeric identifier", example = "1", required = true) Long equipmentId
    ) {
        var equipment = equipmentQueryService.handle(new GetEquipmentByIdQuery(equipmentId));
        if (equipment.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(EquipmentResourceFromEntityAssembler.toResourceFromEntity(equipment.get()));
    }
}