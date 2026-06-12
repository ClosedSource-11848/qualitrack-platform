package com.closedsource.qualitrack.platform.ra.application.internal.commandservices;

import com.closedsource.qualitrack.platform.ra.application.commandservices.RaCommandService;
import com.closedsource.qualitrack.platform.ra.domain.model.aggregates.AuditReport;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.ExportEquipmentLogCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.GenerateBatchReportCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.commands.GenerateComplianceReportCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.entities.AuditLogEntry;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.ReportFormat;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.ReportType;
import com.closedsource.qualitrack.platform.ra.domain.repositories.AuditLogRepository;
import com.closedsource.qualitrack.platform.ra.domain.repositories.AuditReportRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * Application service implementation that executes Reporting and Analysis report commands.
 *
 * <p>This service generates downloadable report content for batch traceability,
 * compliance reporting, and equipment log exports. It also records audit log
 * entries for report generation traceability.</p>
 */
@Service
public class RaCommandServiceImpl implements RaCommandService {

    private final AuditReportRepository auditReportRepository;
    private final AuditLogRepository auditLogRepository;

    public RaCommandServiceImpl(
            AuditReportRepository auditReportRepository,
            AuditLogRepository auditLogRepository
    ) {
        this.auditReportRepository = auditReportRepository;
        this.auditLogRepository = auditLogRepository;
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

    /**
     * Builds report content for a batch report.
     *
     * @param command The report generation command.
     * @return Generated report text content.
     */
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

    /**
     * Builds report content for a compliance report.
     *
     * @param command The report generation command.
     * @return Generated report text content.
     */
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

    /**
     * Builds export content for an equipment log.
     *
     * @param command The export command.
     * @return Generated export text content.
     */
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

    /**
     * Builds a logical generated file path for report persistence.
     *
     * @param prefix Report file prefix.
     * @param format Report output format.
     * @return Logical file path.
     */
    private String buildGeneratedFilePath(String prefix, ReportFormat format) {
        var extension = format == ReportFormat.CSV ? "csv" : "pdf";
        return "generated-reports/%s-%s.%s".formatted(prefix, Instant.now().toEpochMilli(), extension);
    }
}