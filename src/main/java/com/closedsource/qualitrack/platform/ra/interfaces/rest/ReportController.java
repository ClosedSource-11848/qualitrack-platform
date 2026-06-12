package com.closedsource.qualitrack.platform.ra.interfaces.rest;

import com.closedsource.qualitrack.platform.ra.application.commandservices.RaCommandService;
import com.closedsource.qualitrack.platform.ra.application.queryservices.RaQueryService;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetAuditReportByIdQuery;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetAuditReportsByBatchIdQuery;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetAuditReportsByEquipmentIdQuery;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetAuditReportsByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.ReportFormat;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.AuditReportResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.ExportEquipmentLogResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.GenerateBatchReportResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.GenerateComplianceReportResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.AuditReportResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.ExportEquipmentLogCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.GenerateBatchReportCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.GenerateComplianceReportCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes audit report generation and query endpoints.
 */
@RestController
@RequestMapping(value = "/api/v1/reports")
@Tag(name = "Reports", description = "Report generation and audit report endpoints")
public class ReportController {

    private final RaCommandService raCommandService;
    private final RaQueryService raQueryService;

    public ReportController(RaCommandService raCommandService, RaQueryService raQueryService) {
        this.raCommandService = raCommandService;
        this.raQueryService = raQueryService;
    }

    /**
     * Generates a batch report file.
     *
     * @param resource the batch report generation resource
     * @return the generated report file
     */
    @PostMapping(value = "/batches", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Generate batch report")
    public ResponseEntity<?> generateBatchReport(@RequestBody GenerateBatchReportResource resource) {
        var command = GenerateBatchReportCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = raCommandService.handle(command);

        return toFileResponse(
                result,
                "batch-report-%d.%s".formatted(resource.batchId(), toExtensionFromFormat(resource.format())),
                resource.format()
        );
    }

    /**
     * Generates a compliance report file.
     *
     * @param resource the compliance report generation resource
     * @return the generated report file
     */
    @PostMapping(value = "/compliance", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Generate compliance report")
    public ResponseEntity<?> generateComplianceReport(
            @RequestBody GenerateComplianceReportResource resource
    ) {
        var command = GenerateComplianceReportCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = raCommandService.handle(command);

        return toFileResponse(
                result,
                "compliance-report-%d.%s".formatted(resource.laboratoryId(), toExtensionFromFormat(resource.format())),
                resource.format()
        );
    }

    /**
     * Exports equipment audit logs as a report file.
     *
     * @param resource the equipment log export resource
     * @return the generated equipment log file
     */
    @PostMapping(value = "/equipment-logs", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Export equipment logs")
    public ResponseEntity<?> exportEquipmentLogs(@RequestBody ExportEquipmentLogResource resource) {
        var command = ExportEquipmentLogCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = raCommandService.handle(command);

        return toFileResponse(
                result,
                "equipment-log-%d.%s".formatted(resource.equipmentId(), toExtensionFromFormat(resource.format())),
                resource.format()
        );
    }

    /**
     * Retrieves an audit report by its identifier.
     *
     * @param reportId the audit report identifier
     * @return the audit report resource when found
     */
    @GetMapping(value = "/{reportId}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get audit report by ID")
    public ResponseEntity<AuditReportResource> getAuditReportById(@PathVariable Long reportId) {
        var report = raQueryService.handle(new GetAuditReportByIdQuery(reportId));

        if (report.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(AuditReportResourceFromEntityAssembler.toResourceFromEntity(report.get()));
    }

    /**
     * Retrieves audit reports by laboratory identifier.
     *
     * @param laboratoryId the laboratory identifier
     * @return the audit report resources
     */
    @GetMapping(params = "laboratoryId", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get audit reports by laboratory")
    public ResponseEntity<List<AuditReportResource>> getAuditReportsByLaboratoryId(
            @RequestParam Long laboratoryId
    ) {
        var reports = raQueryService.handle(new GetAuditReportsByLaboratoryIdQuery(laboratoryId));

        return ResponseEntity.ok(toAuditReportResources(reports));
    }

    /**
     * Retrieves audit reports by batch identifier.
     *
     * @param batchId the batch identifier
     * @return the audit report resources
     */
    @GetMapping(params = "batchId", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get audit reports by batch")
    public ResponseEntity<List<AuditReportResource>> getAuditReportsByBatchId(@RequestParam Long batchId) {
        var reports = raQueryService.handle(new GetAuditReportsByBatchIdQuery(batchId));

        return ResponseEntity.ok(toAuditReportResources(reports));
    }

    /**
     * Retrieves audit reports by equipment identifier.
     *
     * @param equipmentId the equipment identifier
     * @return the audit report resources
     */
    @GetMapping(params = "equipmentId", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get audit reports by equipment")
    public ResponseEntity<List<AuditReportResource>> getAuditReportsByEquipmentId(
            @RequestParam Long equipmentId
    ) {
        var reports = raQueryService.handle(new GetAuditReportsByEquipmentIdQuery(equipmentId));

        return ResponseEntity.ok(toAuditReportResources(reports));
    }

    private static List<AuditReportResource> toAuditReportResources(
            List<com.closedsource.qualitrack.platform.ra.domain.model.aggregates.AuditReport> reports
    ) {
        return reports.stream()
                .map(AuditReportResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }

    private static ResponseEntity<?> toFileResponse(
            Result<byte[], ApplicationError> result,
            String filename,
            ReportFormat format
    ) {
        return switch (result) {
            case Result.Success<byte[], ApplicationError> success -> ResponseEntity.ok()
                    .contentType(toMediaTypeFromFormat(format))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(filename))
                    .body(success.value());

            case Result.Failure<byte[], ApplicationError> failure ->
                    ErrorResponseAssembler.toErrorResponseFromApplicationError(failure.error());
        };
    }

    private static MediaType toMediaTypeFromFormat(ReportFormat format) {
        if (format == ReportFormat.CSV) return MediaType.parseMediaType("text/csv");
        if (format == ReportFormat.PDF) return MediaType.APPLICATION_PDF;

        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private static String toExtensionFromFormat(ReportFormat format) {
        return format.name().toLowerCase(Locale.ROOT);
    }
}