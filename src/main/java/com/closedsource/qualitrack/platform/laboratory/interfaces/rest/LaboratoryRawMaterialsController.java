package com.closedsource.qualitrack.platform.laboratory.interfaces.rest;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.RawMaterialCommandService;
import com.closedsource.qualitrack.platform.laboratory.application.queryservices.RawMaterialQueryService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateRawMaterialCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.queries.GetLowStockMaterialsByLabIdQuery;
import com.closedsource.qualitrack.platform.laboratory.domain.model.queries.GetRawMaterialsByLabIdQuery;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.CreateRawMaterialResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.RawMaterialResource;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform.RawMaterialResourceFromEntityAssembler;
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
 * REST controller that exposes raw materials inventory for a specific laboratory.
 */
@RestController
@RequestMapping(value = "/api/v1/laboratories/{laboratoryId}/raw-materials", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Laboratory Raw Materials", description = "Laboratory inventory endpoints")
public class LaboratoryRawMaterialsController {
    private final RawMaterialCommandService rawMaterialCommandService;
    private final RawMaterialQueryService rawMaterialQueryService;

    public LaboratoryRawMaterialsController(RawMaterialCommandService rawMaterialCommandService, RawMaterialQueryService rawMaterialQueryService) {
        this.rawMaterialCommandService = rawMaterialCommandService;
        this.rawMaterialQueryService = rawMaterialQueryService;
    }

    @PostMapping
    @Operation(summary = "Register raw material", description = "Registers a new raw material with its initial stock.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Raw material registered successfully", content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Laboratory not found")
    })
    public ResponseEntity<?> createRawMaterial(
            @PathVariable @Parameter(description = "Laboratory identifier", required = true) String laboratoryId,
            @RequestBody CreateRawMaterialResource resource
    ) {
        var command = new CreateRawMaterialCommand(laboratoryId, resource.name(), resource.unit(), resource.initialStock());
        var result = rawMaterialCommandService.handle(command)
                .map(materialId -> new MessageResource("Raw material registered successfully with ID: " + materialId));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all raw materials", description = "Retrieves the complete inventory of raw materials for a laboratory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materials retrieved successfully")
    })
    public ResponseEntity<List<RawMaterialResource>> getRawMaterialsByLaboratoryId(
            @PathVariable @Parameter(description = "Laboratory identifier", required = true) String laboratoryId
    ) {
        var materials = rawMaterialQueryService.handle(new GetRawMaterialsByLabIdQuery(laboratoryId));
        var resources = materials.stream().map(RawMaterialResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock materials", description = "Retrieves raw materials that have fallen below the minimum safety threshold (100 units).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert list retrieved successfully")
    })
    public ResponseEntity<List<RawMaterialResource>> getLowStockRawMaterials(
            @PathVariable @Parameter(description = "Laboratory identifier", required = true) String laboratoryId
    ) {
        var materials = rawMaterialQueryService.handle(new GetLowStockMaterialsByLabIdQuery(laboratoryId));
        var resources = materials.stream().map(RawMaterialResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}