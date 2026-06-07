package com.closedsource.qualitrack.platform.equipment.interfaces.rest;

import com.closedsource.qualitrack.platform.equipment.application.commandservices.MaintenanceCommandService;
import com.closedsource.qualitrack.platform.equipment.application.queryservices.MaintenanceQueryService;
import com.closedsource.qualitrack.platform.equipment.domain.model.commands.RegisterMaintenanceCommand;
import com.closedsource.qualitrack.platform.equipment.domain.model.queries.GetMaintenanceByEquipmentIdQuery;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.MaintenanceRecordResource;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.RegisterMaintenanceResource;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.transform.MaintenanceResourceFromEntityAssembler;
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
 * REST controller that exposes maintenance history endpoints for specific equipment.
 */
@RestController
@RequestMapping(value = "/api/v1/equipments/{equipmentId}/maintenance-records", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Equipment", description = "Equipment management endpoints")
public class EquipmentMaintenanceController {

    private final MaintenanceCommandService maintenanceCommandService;
    private final MaintenanceQueryService maintenanceQueryService;

    public EquipmentMaintenanceController(MaintenanceCommandService maintenanceCommandService, MaintenanceQueryService maintenanceQueryService) {
        this.maintenanceCommandService = maintenanceCommandService;
        this.maintenanceQueryService = maintenanceQueryService;
    }

    @PostMapping
    @Operation(summary = "Register maintenance record", description = "Logs a new technical or calibration intervention for the equipment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Maintenance record saved successfully", content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Equipment not found")
    })
    public ResponseEntity<?> registerMaintenance(
            @PathVariable @Parameter(description = "Equipment numeric identifier", example = "1", required = true) Long equipmentId,
            @RequestBody RegisterMaintenanceResource resource
    ) {
        var command = new RegisterMaintenanceCommand(
                equipmentId,
                resource.maintenanceDate(),
                resource.technicianName(),
                resource.description(),
                resource.type()
        );

        var result = maintenanceCommandService.handle(command)
                .map(recordId -> new MessageResource("Maintenance record registered successfully with ID: " + recordId));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get equipment maintenance history", description = "Retrieves all technical interventions performed on a specific equipment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Maintenance records retrieved successfully")
    })
    public ResponseEntity<List<MaintenanceRecordResource>> getMaintenanceHistory(
            @PathVariable @Parameter(description = "Equipment numeric identifier", example = "1", required = true) Long equipmentId
    ) {
        var records = maintenanceQueryService.handle(new GetMaintenanceByEquipmentIdQuery(equipmentId));
        var resources = records.stream().map(MaintenanceResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}