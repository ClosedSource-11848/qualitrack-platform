package com.closedsource.qualitrack.platform.batch.interfaces.rest;

import com.closedsource.qualitrack.platform.batch.application.commandservices.BatchCommandService;
import com.closedsource.qualitrack.platform.batch.application.queryservices.BatchQueryService;
import com.closedsource.qualitrack.platform.batch.domain.model.aggregates.Batch;
import com.closedsource.qualitrack.platform.batch.domain.model.queries.GetBatchByIdQuery;
import com.closedsource.qualitrack.platform.batch.domain.model.queries.GetBatchesByLabIdQuery;
import com.closedsource.qualitrack.platform.batch.domain.model.queries.GetBatchesByStatusQuery;
import com.closedsource.qualitrack.platform.batch.domain.model.valueobjects.BatchStatus;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.resources.BatchResource;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.resources.CreateBatchResource;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.resources.ReleaseBatchResource;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.resources.RejectBatchResource;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.transform.BatchResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.transform.CreateBatchCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.transform.ReleaseBatchCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.batch.interfaces.rest.transform.RejectBatchCommandFromResourceAssembler;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes production batch resources and lifecycle endpoints.
 */
@RestController
@RequestMapping(value = "/api/v1/batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Batches", description = "Batch management endpoints")
public class BatchController {

    private final BatchCommandService batchCommandService;
    private final BatchQueryService batchQueryService;

    public BatchController(BatchCommandService batchCommandService, BatchQueryService batchQueryService) {
        this.batchCommandService = batchCommandService;
        this.batchQueryService = batchQueryService;
    }

    @PostMapping
    @Operation(summary = "Create production batch", description = "Creates a new production batch for a laboratory product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Batch created successfully", content = @Content(schema = @Schema(implementation = BatchResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Laboratory or product not found"),
            @ApiResponse(responseCode = "409", description = "Batch number already exists")
    })
    public ResponseEntity<?> createBatch(@RequestBody CreateBatchResource resource) {
        var command = CreateBatchCommandFromResourceAssembler.toCommandFromResource(resource);

        var result = batchCommandService.handle(command)
                .flatMap(batchId -> batchQueryService.handle(new GetBatchByIdQuery(batchId))
                        .<Result<Batch, ApplicationError>>map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("Batch", batchId))));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                BatchResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{batchId}")
    @Operation(summary = "Get batch by ID", description = "Retrieves a specific production batch by its numeric identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch found", content = @Content(schema = @Schema(implementation = BatchResource.class))),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    public ResponseEntity<BatchResource> getBatchById(
            @PathVariable
            @Parameter(description = "Batch numeric identifier", example = "1", required = true)
            Long batchId
    ) {
        var batch = batchQueryService.handle(new GetBatchByIdQuery(batchId));

        if (batch.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(BatchResourceFromEntityAssembler.toResourceFromEntity(batch.get()));
    }

    @GetMapping(params = "status")
    @Operation(summary = "Get batches by status", description = "Retrieves production batches by lifecycle status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batches retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid batch status")
    })
    public ResponseEntity<List<BatchResource>> getBatchesByStatus(
            @RequestParam
            @Parameter(description = "Batch lifecycle status", example = "PENDING", required = true)
            BatchStatus status
    ) {
        var batches = batchQueryService.handle(new GetBatchesByStatusQuery(status));

        var resources = batches.stream()
                .map(BatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{batchId}/release")
    @Operation(summary = "Release batch", description = "Releases a production batch after quality control validation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch released successfully", content = @Content(schema = @Schema(implementation = BatchResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid release data"),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    public ResponseEntity<?> releaseBatch(
            @PathVariable
            @Parameter(description = "Batch numeric identifier", example = "1", required = true)
            Long batchId,
            @RequestBody ReleaseBatchResource resource
    ) {
        var command = ReleaseBatchCommandFromResourceAssembler.toCommandFromResource(batchId, resource);

        var result = batchCommandService.handle(command)
                .flatMap(updatedBatchId -> batchQueryService.handle(new GetBatchByIdQuery(updatedBatchId))
                        .<Result<Batch, ApplicationError>>map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("Batch", updatedBatchId))));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                BatchResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{batchId}/reject")
    @Operation(summary = "Reject batch", description = "Rejects a production batch due to quality control or compliance failures.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch rejected successfully", content = @Content(schema = @Schema(implementation = BatchResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid rejection data"),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    public ResponseEntity<?> rejectBatch(
            @PathVariable
            @Parameter(description = "Batch numeric identifier", example = "1", required = true)
            Long batchId,
            @RequestBody RejectBatchResource resource
    ) {
        var command = RejectBatchCommandFromResourceAssembler.toCommandFromResource(batchId, resource);

        var result = batchCommandService.handle(command)
                .flatMap(updatedBatchId -> batchQueryService.handle(new GetBatchByIdQuery(updatedBatchId))
                        .<Result<Batch, ApplicationError>>map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("Batch", updatedBatchId))));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                BatchResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}