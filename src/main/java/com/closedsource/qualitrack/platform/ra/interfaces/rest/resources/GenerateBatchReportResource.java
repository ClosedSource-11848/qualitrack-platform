package com.closedsource.qualitrack.platform.ra.interfaces.rest.resources;

import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.ReportFormat;

/**
 * Resource used to request generation of a production batch report.
 *
 * @param batchId The numeric identifier of the production batch.
 * @param includeTelemetry Indicates whether telemetry data should be included.
 * @param includeDeviations Indicates whether deviation alerts should be included.
 * @param format The requested output format.
 * @param requestedBy The numeric identifier of the requesting user.
 */
public record GenerateBatchReportResource(
        Long batchId,
        Boolean includeTelemetry,
        Boolean includeDeviations,
        ReportFormat format,
        Long requestedBy
) {
}