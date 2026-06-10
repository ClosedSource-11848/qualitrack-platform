package com.closedsource.qualitrack.platform.ca.interfaces.rest;

import com.closedsource.qualitrack.platform.ca.application.commandservices.CaCommandService;
import com.closedsource.qualitrack.platform.ca.application.queryservices.CaQueryService;
import com.closedsource.qualitrack.platform.ca.domain.model.aggregates.DeviationAlert;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetAlertByIdQuery;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetAlertsQuery;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.AlertSeverity;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.AlertStatus;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.AcknowledgeAlertResource;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.CreateDeviationAlertResource;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.DeviationAlertResource;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.ResolveAlertResource;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.transform.AcknowledgeAlertCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.transform.CreateDeviationAlertCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.transform.DeviationAlertResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.transform.ResolveAlertCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes deviation alert resources and lifecycle endpoints.
 */
@RestController
@RequestMapping(value = "/api/v1/alerts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Deviation Alerts", description = "Deviation alert management endpoints")
public class DeviationAlertController {

    private final CaCommandService caCommandService;
    private final CaQueryService caQueryService;

    public DeviationAlertController(CaCommandService caCommandService, CaQueryService caQueryService) {
        this.caCommandService = caCommandService;
        this.caQueryService = caQueryService;
    }

    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody CreateDeviationAlertResource resource) {
        var command = CreateDeviationAlertCommandFromResourceAssembler.toCommandFromResource(resource);

        var result = caCommandService.handle(command)
                .flatMap(alertId -> caQueryService.handle(new GetAlertByIdQuery(alertId))
                        .<Result<DeviationAlert, ApplicationError>>map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("DeviationAlert", alertId))));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                DeviationAlertResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<DeviationAlertResource> getAlertById(@PathVariable Long alertId) {
        var alert = caQueryService.handle(new GetAlertByIdQuery(alertId));

        if (alert.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(DeviationAlertResourceFromEntityAssembler.toResourceFromEntity(alert.get()));
    }

    @GetMapping
    public ResponseEntity<List<DeviationAlertResource>> getAlerts(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity
    ) {
        AlertStatus alertStatus = null;
        AlertSeverity alertSeverity = null;

        try {
            if (status != null && !status.isBlank()) {
                alertStatus = AlertStatus.valueOf(status.toUpperCase());
            }

            if (severity != null && !severity.isBlank()) {
                alertSeverity = AlertSeverity.valueOf(severity.toUpperCase());
            }
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }

        var alerts = caQueryService.handle(new GetAlertsQuery(
                equipmentId,
                batchId,
                alertStatus,
                alertSeverity
        ));

        var resources = alerts.stream()
                .map(DeviationAlertResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{alertId}/acknowledge")
    public ResponseEntity<?> acknowledgeAlert(
            @PathVariable Long alertId,
            @RequestBody AcknowledgeAlertResource resource
    ) {
        var command = AcknowledgeAlertCommandFromResourceAssembler.toCommandFromResource(alertId, resource);

        var result = caCommandService.handle(command)
                .flatMap(updatedAlertId -> caQueryService.handle(new GetAlertByIdQuery(updatedAlertId))
                        .<Result<DeviationAlert, ApplicationError>>map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("DeviationAlert", updatedAlertId))));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                DeviationAlertResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{alertId}/resolve")
    public ResponseEntity<?> resolveAlert(
            @PathVariable Long alertId,
            @RequestBody ResolveAlertResource resource
    ) {
        var command = ResolveAlertCommandFromResourceAssembler.toCommandFromResource(alertId, resource);

        var result = caCommandService.handle(command)
                .flatMap(updatedAlertId -> caQueryService.handle(new GetAlertByIdQuery(updatedAlertId))
                        .<Result<DeviationAlert, ApplicationError>>map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("DeviationAlert", updatedAlertId))));

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                DeviationAlertResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }
}