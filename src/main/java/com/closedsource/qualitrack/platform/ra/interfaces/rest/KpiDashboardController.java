package com.closedsource.qualitrack.platform.ra.interfaces.rest;

import com.closedsource.qualitrack.platform.ra.application.queryservices.RaQueryService;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetKpiDashboardByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.KpiDashboardResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.KpiDashboardResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes KPI dashboard resources.
 */
@RestController
@RequestMapping(value = "/api/v1/kpis", produces = APPLICATION_JSON_VALUE)
@Tag(name = "KPIs", description = "KPI dashboard endpoints")
public class KpiDashboardController {

    private final RaQueryService raQueryService;

    public KpiDashboardController(RaQueryService raQueryService) {
        this.raQueryService = raQueryService;
    }

    /**
     * Retrieves the latest KPI dashboard for a laboratory.
     *
     * @param laboratoryId the laboratory numeric identifier
     * @return the KPI dashboard resource when found
     */
    @GetMapping
    @Operation(summary = "Get KPI dashboard by laboratory")
    public ResponseEntity<KpiDashboardResource> getKpiDashboardByLaboratoryId(
            @RequestParam Long laboratoryId
    ) {
        var dashboard = raQueryService.handle(new GetKpiDashboardByLaboratoryIdQuery(laboratoryId));

        if (dashboard.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                KpiDashboardResourceFromEntityAssembler.toResourceFromEntity(dashboard.get())
        );
    }
}