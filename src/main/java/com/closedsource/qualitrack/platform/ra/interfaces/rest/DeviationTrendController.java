package com.closedsource.qualitrack.platform.ra.interfaces.rest;

import com.closedsource.qualitrack.platform.ra.application.commandservices.RaCommandService;
import com.closedsource.qualitrack.platform.ra.application.queryservices.RaQueryService;
import com.closedsource.qualitrack.platform.ra.domain.model.entities.DeviationTrend;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetDeviationTrendsByEquipmentIdQuery;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.CalculateDeviationTrendResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.DeviationTrendResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.CalculateDeviationTrendCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.DeviationTrendResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes deviation trend resources.
 */
@RestController
@RequestMapping(value = "/api/v1/deviation-trends", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Deviation Trends", description = "Deviation trend analysis endpoints")
public class DeviationTrendController {

    private final RaCommandService raCommandService;
    private final RaQueryService raQueryService;

    public DeviationTrendController(RaCommandService raCommandService, RaQueryService raQueryService) {
        this.raCommandService = raCommandService;
        this.raQueryService = raQueryService;
    }

    /**
     * Calculates and stores a deviation trend for an equipment parameter.
     *
     * @param resource deviation trend calculation request resource
     * @return the calculated deviation trend resource
     */
    @PostMapping(value = "/calculate", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Calculate deviation trend")
    public ResponseEntity<?> calculateDeviationTrend(
            @RequestBody CalculateDeviationTrendResource resource
    ) {
        var command = CalculateDeviationTrendCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = raCommandService.handle(command);

        return toDeviationTrendResponse(result);
    }

    /**
     * Retrieves deviation trends for an equipment.
     *
     * @param equipmentId the equipment numeric identifier
     * @return the deviation trend resources
     */
    @GetMapping
    @Operation(summary = "Get deviation trends by equipment")
    public ResponseEntity<List<DeviationTrendResource>> getDeviationTrendsByEquipmentId(
            @RequestParam Long equipmentId
    ) {
        var trends = raQueryService.handle(new GetDeviationTrendsByEquipmentIdQuery(equipmentId));

        var resources = trends.stream()
                .map(DeviationTrendResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    private static ResponseEntity<?> toDeviationTrendResponse(
            Result<DeviationTrend, ApplicationError> result
    ) {
        return switch (result) {
            case Result.Success<DeviationTrend, ApplicationError> success ->
                    ResponseEntity.status(201).body(
                            DeviationTrendResourceFromEntityAssembler.toResourceFromEntity(success.value())
                    );

            case Result.Failure<DeviationTrend, ApplicationError> failure ->
                    ErrorResponseAssembler.toErrorResponseFromApplicationError(failure.error());
        };
    }
}