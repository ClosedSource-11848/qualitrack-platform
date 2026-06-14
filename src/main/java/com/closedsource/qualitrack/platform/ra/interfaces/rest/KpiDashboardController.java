package com.closedsource.qualitrack.platform.ra.interfaces.rest;

import com.closedsource.qualitrack.platform.ra.application.commandservices.RaCommandService;
import com.closedsource.qualitrack.platform.ra.application.queryservices.RaQueryService;
import com.closedsource.qualitrack.platform.ra.domain.model.aggregates.KpiDashboard;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetKpiDashboardByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.CalculateKpiDashboardResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.KpiDashboardResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.CalculateKpiDashboardCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.KpiDashboardResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
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

    private final RaCommandService raCommandService;
    private final RaQueryService raQueryService;

    public KpiDashboardController(RaCommandService raCommandService, RaQueryService raQueryService) {
        this.raCommandService = raCommandService;
        this.raQueryService = raQueryService;
    }

    /**
     * Calculates and stores a KPI dashboard snapshot for a laboratory.
     *
     * @param resource KPI dashboard calculation request resource
     * @return the calculated KPI dashboard resource
     */
    @PostMapping(value = "/calculate", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Calculate KPI dashboard")
    public ResponseEntity<?> calculateKpiDashboard(
            @RequestBody CalculateKpiDashboardResource resource
    ) {
        var command = CalculateKpiDashboardCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = raCommandService.handle(command);

        return toKpiDashboardResponse(result);
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

    private static ResponseEntity<?> toKpiDashboardResponse(
            Result<KpiDashboard, ApplicationError> result
    ) {
        return switch (result) {
            case Result.Success<KpiDashboard, ApplicationError> success ->
                    ResponseEntity.status(201).body(
                            KpiDashboardResourceFromEntityAssembler.toResourceFromEntity(success.value())
                    );

            case Result.Failure<KpiDashboard, ApplicationError> failure ->
                    ErrorResponseAssembler.toErrorResponseFromApplicationError(failure.error());
        };
    }
}