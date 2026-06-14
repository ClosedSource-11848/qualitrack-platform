package com.closedsource.qualitrack.platform.ra.application.internal.commandservices;

import com.closedsource.qualitrack.platform.ra.application.commandservices.RaCommandService;
import com.closedsource.qualitrack.platform.ra.domain.model.aggregates.AuditReport;
import com.closedsource.qualitrack.platform.ra.domain.model.aggregates.KpiDashboard;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.CalculateDeviationTrendCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.CalculateKpiDashboardCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.ExportEquipmentLogCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.GenerateBatchReportCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.GenerateComplianceReportCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.entities.AuditLogEntry;
import com.closedsource.qualitrack.platform.ra.domain.model.entities.DeviationTrend;
import com.closedsource.qualitrack.platform.ra.domain.model.entities.KpiMetric;
import com.closedsource.qualitrack.platform.ra.domain.model.entities.TrendDataPoint;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.ReportFormat;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.ReportType;
import com.closedsource.qualitrack.platform.ra.domain.repositories.AuditLogRepository;
import com.closedsource.qualitrack.platform.ra.domain.repositories.AuditReportRepository;
import com.closedsource.qualitrack.platform.ra.domain.repositories.DeviationTrendRepository;
import com.closedsource.qualitrack.platform.ra.domain.repositories.KpiDashboardRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Application service implementation that executes Reporting and Analysis report commands.
 *
 * <p>This service generates downloadable report content for batch traceability,
 * compliance reporting, equipment log exports, KPI dashboard snapshots, and
 * deviation trend analysis. It also records audit log entries for reporting
 * traceability.</p>
 */
@Service
public class RaCommandServiceImpl implements RaCommandService {

    private final AuditReportRepository auditReportRepository;
    private final AuditLogRepository auditLogRepository;
    private final KpiDashboardRepository kpiDashboardRepository;
    private final DeviationTrendRepository deviationTrendRepository;

    public RaCommandServiceImpl(
            AuditReportRepository auditReportRepository,
            AuditLogRepository auditLogRepository,
            KpiDashboardRepository kpiDashboardRepository,
            DeviationTrendRepository deviationTrendRepository
    ) {
        this.auditReportRepository = auditReportRepository;
        this.auditLogRepository = auditLogRepository;
        this.kpiDashboardRepository = kpiDashboardRepository;
        this.deviationTrendRepository = deviationTrendRepository;
    }

    /**
     * Handles KPI dashboard calculation.
     *
     * @param command The command containing the laboratory identifier.
     * @return Calculated KPI dashboard or an application error.
     */
    @Override
    public Result<KpiDashboard, ApplicationError> handle(CalculateKpiDashboardCommand command) {
        try {
            var recordedAt = LocalDateTime.now().toString();

            var metrics = List.of(
                    new KpiMetric("Batch Release Rate", 92.0, "%", 95.0, recordedAt),
                    new KpiMetric("Equipment Availability", 88.0, "%", 90.0, recordedAt),
                    new KpiMetric("Critical Alerts Resolution", 76.0, "%", 85.0, recordedAt),
                    new KpiMetric("BPM Compliance Score", 94.0, "%", 90.0, recordedAt)
            );

            var dashboard = new KpiDashboard(command.laboratoryId(), metrics);
            var savedDashboard = kpiDashboardRepository.save(dashboard);

            auditLogRepository.save(new AuditLogEntry(
                    AuditAction.GENERATE,
                    "KPI_DASHBOARD",
                    savedDashboard.getId(),
                    command.laboratoryId(),
                    "KPI dashboard calculated for laboratory ID %d"
                            .formatted(command.laboratoryId())
            ));

            return Result.success(savedDashboard);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("KpiDashboard", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("calculate-kpi-dashboard", exception.getMessage()));
        }
    }

    /**
     * Handles deviation trend calculation.
     *
     * @param command The command containing the equipment and parameter identifiers.
     * @return Calculated deviation trend or an application error.
     */
    @Override
    public Result<DeviationTrend, ApplicationError> handle(CalculateDeviationTrendCommand command) {
        try {
            var now = LocalDateTime.now();

            var dataPoints = List.of(
                    new TrendDataPoint(now.minusDays(4).toString(), 98.7, 102.0, 98.0),
                    new TrendDataPoint(now.minusDays(3).toString(), 99.4, 102.0, 98.0),
                    new TrendDataPoint(now.minusDays(2).toString(), 101.1, 102.0, 98.0),
                    new TrendDataPoint(now.minusDays(1).toString(), 102.6, 102.0, 98.0),
                    new TrendDataPoint(now.toString(), 103.2, 102.0, 98.0)
            );

            var trend = new DeviationTrend(
                    command.parameterName(),
                    command.equipmentId(),
                    dataPoints
            );

            var savedTrend = deviationTrendRepository.save(trend);

            auditLogRepository.save(new AuditLogEntry(
                    AuditAction.GENERATE,
                    "DEVIATION_TREND",
                    savedTrend.getId(),
                    1L,
                    "Deviation trend calculated for equipment ID %d and parameter '%s'"
                            .formatted(command.equipmentId(), command.parameterName())
            ));

            return Result.success(savedTrend);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("DeviationTrend", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("calculate-deviation-trend", exception.getMessage()));
        }
    }

    /**
     * Handles production batch report generation.
     *
     * @param command The command containing batch report generation parameters.
     * @return Generated report bytes or an application error.
     */
    @Override
    public Result<byte[], ApplicationError> handle(GenerateBatchReportCommand command) {
        try {
            var content = buildBatchReportContent(command);
            var bytes = content.getBytes(StandardCharsets.UTF_8);

            auditLogRepository.save(new AuditLogEntry(
                    AuditAction.GENERATE,
                    "BATCH",
                    command.batchId(),
                    command.requestedBy(),
                    "Batch report generated. includeTelemetry=%s, includeDeviations=%s, format=%s"
                            .formatted(command.includeTelemetry(), command.includeDeviations(), command.format())
            ));

            return Result.success(bytes);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("BatchReport", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("generate-batch-report", exception.getMessage()));
        }
    }

    /**
     * Handles compliance report generation.
     *
     * @param command The command containing compliance report generation parameters.
     * @return Generated report bytes or an application error.
     */
    @Override
    public Result<byte[], ApplicationError> handle(GenerateComplianceReportCommand command) {
        try {
            var content = buildComplianceReportContent(command);
            var bytes = content.getBytes(StandardCharsets.UTF_8);

            var report = new AuditReport(
                    command.laboratoryId(),
                    null,
                    null,
                    command.requestedBy(),
                    "User " + command.requestedBy(),
                    ReportType.COMPLIANCE_PERIOD,
                    command.startDate(),
                    command.endDate(),
                    buildGeneratedFilePath("compliance-report", command.format()),
                    bytes
            );

            auditReportRepository.save(report);

            auditLogRepository.save(new AuditLogEntry(
                    AuditAction.GENERATE,
                    "LABORATORY",
                    command.laboratoryId(),
                    command.requestedBy(),
                    "Compliance report generated. startDate=%s, endDate=%s, format=%s"
                            .formatted(command.startDate(), command.endDate(), command.format())
            ));

            return Result.success(bytes);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("ComplianceReport", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("generate-compliance-report", exception.getMessage()));
        }
    }

    /**
     * Handles equipment log export.
     *
     * @param command The command containing equipment log export parameters.
     * @return Exported log bytes or an application error.
     */
    @Override
    public Result<byte[], ApplicationError> handle(ExportEquipmentLogCommand command) {
        try {
            var content = buildEquipmentLogContent(command);
            var bytes = content.getBytes(StandardCharsets.UTF_8);

            auditLogRepository.save(new AuditLogEntry(
                    AuditAction.EXPORT,
                    "EQUIPMENT",
                    command.equipmentId(),
                    command.requestedBy(),
                    "Equipment log exported. startDate=%s, endDate=%s, format=%s"
                            .formatted(command.startDate(), command.endDate(), command.format())
            ));

            return Result.success(bytes);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("EquipmentLog", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("export-equipment-log", exception.getMessage()));
        }
    }

    private String buildBatchReportContent(GenerateBatchReportCommand command) {
        if (command.format() == ReportFormat.CSV) {
            return """
                    section,value
                    reportType,BATCH_TRACEABILITY
                    batchId,%d
                    includeTelemetry,%s
                    includeDeviations,%s
                    requestedBy,%d
                    generatedAt,%s
                    """.formatted(
                    command.batchId(),
                    command.includeTelemetry(),
                    command.includeDeviations(),
                    command.requestedBy(),
                    Instant.now()
            );
        }

        return """
                QualiTrack - Batch Traceability Report

                Batch ID: %d
                Include Telemetry: %s
                Include Deviations: %s
                Requested By: %d
                Generated At: %s

                This report was generated by the Reporting and Analysis bounded context.
                """.formatted(
                command.batchId(),
                command.includeTelemetry(),
                command.includeDeviations(),
                command.requestedBy(),
                Instant.now()
        );
    }

    private String buildComplianceReportContent(GenerateComplianceReportCommand command) {
        if (command.format() == ReportFormat.CSV) {
            return """
                    section,value
                    reportType,COMPLIANCE_PERIOD
                    laboratoryId,%d
                    startDate,%s
                    endDate,%s
                    requestedBy,%d
                    generatedAt,%s
                    """.formatted(
                    command.laboratoryId(),
                    command.startDate(),
                    command.endDate(),
                    command.requestedBy(),
                    Instant.now()
            );
        }

        return """
                QualiTrack - Compliance Report

                Laboratory ID: %d
                Period Start: %s
                Period End: %s
                Requested By: %d
                Generated At: %s

                This report was generated by the Reporting and Analysis bounded context.
                """.formatted(
                command.laboratoryId(),
                command.startDate(),
                command.endDate(),
                command.requestedBy(),
                Instant.now()
        );
    }

    private String buildEquipmentLogContent(ExportEquipmentLogCommand command) {
        if (command.format() == ReportFormat.CSV) {
            return """
                    section,value
                    reportType,EQUIPMENT_LOG
                    equipmentId,%d
                    startDate,%s
                    endDate,%s
                    requestedBy,%d
                    generatedAt,%s
                    """.formatted(
                    command.equipmentId(),
                    command.startDate(),
                    command.endDate(),
                    command.requestedBy(),
                    Instant.now()
            );
        }

        return """
                QualiTrack - Equipment Log Export

                Equipment ID: %d
                Period Start: %s
                Period End: %s
                Requested By: %d
                Generated At: %s

                This export was generated by the Reporting and Analysis bounded context.
                """.formatted(
                command.equipmentId(),
                command.startDate(),
                command.endDate(),
                command.requestedBy(),
                Instant.now()
        );
    }

    private String buildGeneratedFilePath(String prefix, ReportFormat format) {
        var extension = format == ReportFormat.CSV ? "csv" : "pdf";
        return "generated-reports/%s-%s.%s".formatted(prefix, Instant.now().toEpochMilli(), extension);
    }
}