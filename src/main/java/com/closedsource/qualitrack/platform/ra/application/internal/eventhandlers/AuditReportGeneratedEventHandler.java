package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ra.domain.model.events.AuditReportGeneratedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link AuditReportGeneratedEvent}.
 *
 * <p>Listens for audit report generation events to perform downstream side
 * effects such as notifications, audit indexing, or external storage
 * synchronization.</p>
 */
@Service
@Slf4j
public class AuditReportGeneratedEventHandler {

    /**
     * Default constructor.
     */
    public AuditReportGeneratedEventHandler() {
    }

    /**
     * Handles the {@link AuditReportGeneratedEvent}.
     *
     * @param event the {@link AuditReportGeneratedEvent} published by the RA bounded context
     */
    @EventListener(AuditReportGeneratedEvent.class)
    public void on(AuditReportGeneratedEvent event) {
        log.info(
                "Audit report generated: Report ID='{}', Laboratory ID='{}', Batch ID='{}', Equipment ID='{}', GeneratedBy='{}', Type='{}', GeneratedAt='{}'.",
                event.reportId(),
                event.laboratoryId(),
                event.batchId(),
                event.equipmentId(),
                event.generatedBy(),
                event.reportType(),
                event.generatedAt()
        );

        // TODO: In the future, notify external storage or document indexing services.
    }
}