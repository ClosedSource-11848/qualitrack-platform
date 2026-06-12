package com.closedsource.qualitrack.platform.ra.interfaces.rest;

import com.closedsource.qualitrack.platform.ra.application.queryservices.RaQueryService;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetDeviationTrendsByEquipmentIdQuery;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.DeviationTrendResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.DeviationTrendResourceFromEntityAssembler;
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

    private final RaQueryService raQueryService;

    public DeviationTrendController(RaQueryService raQueryService) {
        this.raQueryService = raQueryService;
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
}