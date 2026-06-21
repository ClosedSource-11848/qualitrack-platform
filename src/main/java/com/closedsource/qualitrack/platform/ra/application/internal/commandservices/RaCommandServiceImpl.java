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
                    "KPI dashboard calculated for laboratory ID %d".formatted(command.laboratoryId())
            ));

            return Result.success(savedDashboard);

        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("KpiDashboard", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected("calculate-kpi-dashboard", exception.getMessage()));
        }
    }

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

    @Override
    public Result<byte[], ApplicationError> handle(GenerateBatchReportCommand command) {
        try {
            var content = buildBatchReportContent(command);
            var bytes = buildReportBytes(
                    command.format(),
                    "QualiTrack - Batch Traceability Report",
                    content
            );

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

    @Override
    public Result<byte[], ApplicationError> handle(GenerateComplianceReportCommand command) {
        try {
            var content = buildComplianceReportContent(command);
            var bytes = buildReportBytes(
                    command.format(),
                    "QualiTrack - Compliance Report",
                    content
            );

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

    @Override
    public Result<byte[], ApplicationError> handle(ExportEquipmentLogCommand command) {
        try {
            var content = buildEquipmentLogContent(command);
            var bytes = buildReportBytes(
                    command.format(),
                    "QualiTrack - Equipment Log Export",
                    content
            );

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

    private byte[] buildReportBytes(ReportFormat format, String title, String content) {
        if (format == ReportFormat.CSV) {
            return content.getBytes(StandardCharsets.UTF_8);
        }

        return buildSimplePdf(title, content);
    }

    private byte[] buildSimplePdf(String title, String content) {
        var stream = new StringBuilder();

        stream.append("BT\n");
        stream.append("/F1 18 Tf\n");
        stream.append("72 750 Td\n");
        stream.append("(").append(escapePdfText(title)).append(") Tj\n");
        stream.append("/F1 11 Tf\n");
        stream.append("0 -30 Td\n");

        content.lines()
                .filter(line -> !line.isBlank())
                .limit(32)
                .forEach(line -> {
                    var safeLine = line.length() > 95 ? line.substring(0, 92) + "..." : line;
                    stream.append("(").append(escapePdfText(safeLine)).append(") Tj\n");
                    stream.append("0 -16 Td\n");
                });

        stream.append("ET\n");

        var streamText = stream.toString();
        var streamLength = streamText.getBytes(StandardCharsets.UTF_8).length;

        var objects = new String[] {
                "<< /Type /Catalog /Pages 2 0 R >>",
                "<< /Type /Pages /Kids [3 0 R] /Count 1 >>",
                "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>",
                "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>",
                "<< /Length %d >>\nstream\n%sendstream".formatted(streamLength, streamText)
        };

        var pdf = new StringBuilder("%PDF-1.4\n");
        var offsets = new int[objects.length + 1];

        for (int index = 0; index < objects.length; index++) {
            offsets[index + 1] = pdf.toString().getBytes(StandardCharsets.UTF_8).length;
            pdf.append(index + 1).append(" 0 obj\n");
            pdf.append(objects[index]).append("\n");
            pdf.append("endobj\n");
        }

        var xrefOffset = pdf.toString().getBytes(StandardCharsets.UTF_8).length;

        pdf.append("xref\n");
        pdf.append("0 ").append(objects.length + 1).append("\n");
        pdf.append("0000000000 65535 f \n");

        for (int index = 1; index < offsets.length; index++) {
            pdf.append("%010d 00000 n \n".formatted(offsets[index]));
        }

        pdf.append("trailer\n");
        pdf.append("<< /Size ").append(objects.length + 1).append(" /Root 1 0 R >>\n");
        pdf.append("startxref\n");
        pdf.append(xrefOffset).append("\n");
        pdf.append("%%EOF");

        return pdf.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapePdfText(String value) {
        if (value == null) return "";

        return value
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replaceAll("[^\\x20-\\x7E]", "?");
    }

    private String buildGeneratedFilePath(String prefix, ReportFormat format) {
        var extension = format == ReportFormat.CSV ? "csv" : "pdf";
        return "generated-reports/%s-%s.%s".formatted(prefix, Instant.now().toEpochMilli(), extension);
    }
}