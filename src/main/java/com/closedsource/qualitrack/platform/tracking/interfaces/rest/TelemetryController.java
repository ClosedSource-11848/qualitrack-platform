package com.closedsource.qualitrack.platform.tracking.interfaces.rest;

import com.closedsource.qualitrack.platform.tracking.application.commandservices.TrackingCommandService;
import com.closedsource.qualitrack.platform.tracking.application.queryservices.TrackingQueryService;
import com.closedsource.qualitrack.platform.tracking.domain.model.entities.EquipmentTelemetryStatus;
import com.closedsource.qualitrack.platform.tracking.domain.model.queries.GetEquipmentTelemetryStatusByEquipmentIdQuery;
import com.closedsource.qualitrack.platform.tracking.domain.model.queries.GetLatestMeasurementsQuery;
import com.closedsource.qualitrack.platform.tracking.domain.model.queries.GetTelemetryHistoryQuery;
import com.closedsource.qualitrack.platform.tracking.domain.model.valueobjects.TelemetryStatus;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.EquipmentTelemetryStatusResource;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.MeasurementResource;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.RecordMeasurementResource;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.RecordTelemetryHistoryPointResource;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.TelemetryHistoryPointResource;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.UpdateEquipmentTelemetryStatusResource;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform.EquipmentTelemetryStatusResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform.MeasurementResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform.RecordMeasurementCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform.RecordTelemetryHistoryPointCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform.TelemetryHistoryPointResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform.UpdateEquipmentTelemetryStatusCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes telemetry tracking endpoints.
 *
 * @remarks
 * This controller provides the HTTP contract consumed by the frontend Tracking
 * bounded context. It supports latest measurements, current equipment telemetry
 * status, and historical telemetry points.
 */
@RestController
@RequestMapping(value = "/api/v1/telemetry", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Tracking", description = "Telemetry tracking endpoints")
public class TelemetryController {

    private final TrackingCommandService trackingCommandService;
    private final TrackingQueryService trackingQueryService;

    /**
     * Creates a new TelemetryController.
     *
     * @param trackingCommandService Tracking command service
     * @param trackingQueryService Tracking query service
     */
    public TelemetryController(
            TrackingCommandService trackingCommandService,
            TrackingQueryService trackingQueryService
    ) {
        this.trackingCommandService = trackingCommandService;
        this.trackingQueryService = trackingQueryService;
    }

    /**
     * Retrieves latest telemetry measurements.
     *
     * @param equipmentId optional equipment identifier
     * @return latest measurement resources
     */
    @GetMapping("/measurements")
    @Operation(summary = "Get latest telemetry measurements")
    public ResponseEntity<List<MeasurementResource>> getLatestMeasurements(
            @RequestParam(required = false) Long equipmentId
    ) {
        var measurements = trackingQueryService.handle(new GetLatestMeasurementsQuery(equipmentId));

        var resources = measurements.stream()
                .map(MeasurementResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Records a telemetry measurement.
     *
     * @param resource the record measurement resource
     * @return the recorded measurement identifier
     */
    @PostMapping(value = "/measurements", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Record telemetry measurement")
    public ResponseEntity<?> recordMeasurement(@RequestBody RecordMeasurementResource resource) {
        var command = RecordMeasurementCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = trackingCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                measurementId -> measurementId,
                HttpStatus.CREATED
        );
    }

    /**
     * Retrieves the current telemetry status for an equipment.
     *
     * @param equipmentId the equipment identifier
     * @return current equipment telemetry status resource
     */
    @GetMapping("/status/{equipmentId}")
    @Operation(summary = "Get equipment telemetry status")
    public ResponseEntity<EquipmentTelemetryStatusResource> getEquipmentTelemetryStatus(
            @PathVariable Long equipmentId
    ) {
        var status = trackingQueryService.handle(
                new GetEquipmentTelemetryStatusByEquipmentIdQuery(equipmentId)
        ).orElseGet(() -> EquipmentTelemetryStatus.update(
                equipmentId,
                false,
                TelemetryStatus.OFFLINE,
                null
        ));

        return ResponseEntity.ok(
                EquipmentTelemetryStatusResourceFromEntityAssembler.toResourceFromEntity(status)
        );
    }

    /**
     * Updates the current telemetry status for an equipment.
     *
     * @param resource the update equipment telemetry status resource
     * @return the saved status identifier
     */
    @PutMapping(value = "/status", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update equipment telemetry status")
    public ResponseEntity<?> updateEquipmentTelemetryStatus(
            @RequestBody UpdateEquipmentTelemetryStatusResource resource
    ) {
        var command = UpdateEquipmentTelemetryStatusCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = trackingCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                statusId -> statusId,
                HttpStatus.OK
        );
    }

    /**
     * Retrieves historical telemetry points.
     *
     * @param equipmentId optional equipment identifier
     * @param from optional start timestamp
     * @param to optional end timestamp
     * @return telemetry history point resources
     */
    @GetMapping("/history")
    @Operation(summary = "Get telemetry history")
    public ResponseEntity<List<TelemetryHistoryPointResource>> getTelemetryHistory(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        var historyPoints = trackingQueryService.handle(
                new GetTelemetryHistoryQuery(equipmentId, from, to)
        );

        var resources = historyPoints.stream()
                .map(TelemetryHistoryPointResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Records a telemetry history point.
     *
     * @param resource the record telemetry history point resource
     * @return the recorded telemetry history point identifier
     */
    @PostMapping(value = "/history", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Record telemetry history point")
    public ResponseEntity<?> recordTelemetryHistoryPoint(
            @RequestBody RecordTelemetryHistoryPointResource resource
    ) {
        var command = RecordTelemetryHistoryPointCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = trackingCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                historyPointId -> historyPointId,
                HttpStatus.CREATED
        );
    }
}