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
@Tag(name = "Laboratories", description = "Laboratory management endpoints")
public class LaboratoryRawMaterialsController {

    private final RawMaterialCommandService rawMaterialCommandService;
    private final RawMaterialQueryService rawMaterialQueryService;

    public LaboratoryRawMaterialsController(RawMaterialCommandService rawMaterialCommandService, RawMaterialQueryService rawMaterialQueryService) {
        this.rawMaterialCommandService = rawMaterialCommandService;
        this.rawMaterialQueryService = rawMaterialQueryService;
    }

    @PostMapping
    @Operation(summary = "Register raw material", description = "Registers a new raw material with its initial stock and traceability data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Raw material registered successfully",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Laboratory not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflict - Raw material code already exists",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> createRawMaterial(
            @PathVariable @Parameter(description = "Laboratory numeric identifier", example = "1", required = true) Long laboratoryId,
            @RequestBody CreateRawMaterialResource resource
    ) {
        var command = new CreateRawMaterialCommand(
                laboratoryId,
                resource.name(),
                resource.code(),
                resource.supplier(),
                resource.batchNumber(),
                resource.expirationDate(),
                resource.quantityInStock(),
                resource.unit(),
                resource.minimumStock()
        );

        var result = rawMaterialCommandService.handle(command)
                .map(materialId -> new MessageResource("Raw material registered successfully with ID: " + materialId));

        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get raw materials", description = "Retrieves the inventory of raw materials. Can be filtered to show only items below their safety threshold.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materials retrieved successfully")
    })
    public ResponseEntity<List<RawMaterialResource>> getRawMaterials(
            @PathVariable @Parameter(description = "Laboratory numeric identifier", example = "1", required = true) Long laboratoryId,
            @RequestParam(required = false, defaultValue = "false") @Parameter(description = "Set to true to filter only materials with low stock") boolean lowStock
    ) {
        List<RawMaterialResource> resources;

        if (lowStock) {
            var materials = rawMaterialQueryService.handle(new GetLowStockMaterialsByLabIdQuery(laboratoryId));
            resources = materials.stream().map(RawMaterialResourceFromEntityAssembler::toResourceFromEntity).toList();
        } else {
            var materials = rawMaterialQueryService.handle(new GetRawMaterialsByLabIdQuery(laboratoryId));
            resources = materials.stream().map(RawMaterialResourceFromEntityAssembler::toResourceFromEntity).toList();
        }

        return ResponseEntity.ok(resources);
    }
}