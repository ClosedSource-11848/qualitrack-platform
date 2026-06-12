package com.closedsource.qualitrack.platform.ra.interfaces.rest;

import com.closedsource.qualitrack.platform.ra.application.commandservices.AuditLogCommandService;
import com.closedsource.qualitrack.platform.ra.application.queryservices.RaQueryService;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetAuditLogQuery;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.AuditLogEntryResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.RecordAuditLogEntryResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.AuditLogEntryResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.RecordAuditLogEntryCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes audit log resources.
 */
@RestController
@RequestMapping(value = "/api/v1/audit-log", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Audit Log", description = "Audit log query and recording endpoints")
public class AuditLogController {

    private final RaQueryService raQueryService;
    private final AuditLogCommandService auditLogCommandService;

    public AuditLogController(
            RaQueryService raQueryService,
            AuditLogCommandService auditLogCommandService
    ) {
        this.raQueryService = raQueryService;
        this.auditLogCommandService = auditLogCommandService;
    }

    /**
     * Retrieves audit log entries using optional filters.
     *
     * @param equipmentId optional equipment identifier
     * @param batchId optional batch identifier
     * @param dateFrom optional start date
     * @param dateTo optional end date
     * @return the audit log entry resources
     */
    @GetMapping
    @Operation(summary = "Get audit log entries")
    public ResponseEntity<List<AuditLogEntryResource>> getAuditLog(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        var entries = raQueryService.handle(new GetAuditLogQuery(equipmentId, batchId, dateFrom, dateTo));

        var resources = entries.stream()
                .map(AuditLogEntryResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Records a new audit log entry.
     *
     * @param resource the audit log entry request resource
     * @return the recorded audit log entry resource
     */
    @PostMapping
    @Operation(summary = "Record audit log entry")
    public ResponseEntity<?> recordAuditLogEntry(@RequestBody RecordAuditLogEntryResource resource) {
        var command = RecordAuditLogEntryCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = auditLogCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                AuditLogEntryResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }
}