package com.closedsource.qualitrack.platform.ra.interfaces.rest;

import com.closedsource.qualitrack.platform.ra.application.queryservices.RaQueryService;
import com.closedsource.qualitrack.platform.ra.domain.model.queries.GetAuditReportByIdQuery;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.AuditReportResource;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.transform.AuditReportResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes audit report resources.
 */
@RestController
@RequestMapping(value = "/api/v1/reports", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Audit report query endpoints")
public class ReportController {

    private final RaQueryService raQueryService;

    public ReportController(RaQueryService raQueryService) {
        this.raQueryService = raQueryService;
    }

    @GetMapping(value = "/{reportId}")
    @Operation(summary = "Get audit report by ID")
    public ResponseEntity<AuditReportResource> getAuditReportById(
            @PathVariable Long reportId
    ) {
        var report = raQueryService.handle(new GetAuditReportByIdQuery(reportId));

        if (report.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                AuditReportResourceFromEntityAssembler.toResourceFromEntity(report.get())
        );
    }
}