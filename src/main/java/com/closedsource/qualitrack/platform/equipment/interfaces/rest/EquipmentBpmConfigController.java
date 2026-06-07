package com.closedsource.qualitrack.platform.equipment.interfaces.rest;

import com.closedsource.qualitrack.platform.equipment.application.commandservices.BpmConfigCommandService;
import com.closedsource.qualitrack.platform.equipment.application.queryservices.BpmConfigQueryService;
import com.closedsource.qualitrack.platform.equipment.domain.model.commands.ConfigureBpmParametersCommand;
import com.closedsource.qualitrack.platform.equipment.domain.model.queries.GetBpmParameterConfigsByEquipmentIdQuery;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.BpmParameterConfigResource;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.ConfigureBpmResource;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.transform.BpmConfigResourceFromEntityAssembler;
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
 * REST controller that exposes BPM parameter configuration endpoints for specific equipment.
 */
@RestController
@RequestMapping(value = "/api/v1/equipments/{equipmentId}/bpm-configs", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Equipment", description = "Equipment management endpoints")
public class EquipmentBpmConfigController {

    private final BpmConfigCommandService bpmConfigCommandService;
    private final BpmConfigQueryService bpmConfigQueryService;

    public EquipmentBpmConfigController(BpmConfigCommandService bpmConfigCommandService, BpmConfigQueryService bpmConfigQueryService) {
        this.bpmConfigCommandService = bpmConfigCommandService;
        this.bpmConfigQueryService = bpmConfigQueryService;
    }

    @PostMapping
    @Operation(summary = "Configure BPM parameter", description = "Creates or updates an acceptable operational range for a specific parameter (e.g., Temperature, Pressure).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "BPM configuration saved successfully", content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Equipment not found")
    })
    public ResponseEntity<?> configureBpmParameter(
            @PathVariable @Parameter(description = "Equipment numeric identifier", example = "1", required = true) Long equipmentId,
            @RequestBody ConfigureBpmResource resource
    ) {
        // Tomamos el equipmentId de la URL de forma segura
        var command = new ConfigureBpmParametersCommand(
                equipmentId,
                resource.parameterName(),
                resource.minValue(),
                resource.maxValue(),
                resource.unit()
        );

        var result = bpmConfigCommandService.handle(command)
                .map(configId -> new MessageResource("BPM Parameter configuration saved successfully with ID: " + configId));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get equipment BPM configurations", description = "Retrieves all parameter thresholds configured for a specific equipment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configurations retrieved successfully")
    })
    public ResponseEntity<List<BpmParameterConfigResource>> getBpmConfigsByEquipmentId(
            @PathVariable @Parameter(description = "Equipment numeric identifier", example = "1", required = true) Long equipmentId
    ) {
        var configs = bpmConfigQueryService.handle(new GetBpmParameterConfigsByEquipmentIdQuery(equipmentId));
        var resources = configs.stream().map(BpmConfigResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}