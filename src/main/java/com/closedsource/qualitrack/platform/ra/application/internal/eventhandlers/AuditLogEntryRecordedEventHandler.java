package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ra.domain.model.events.AuditLogEntryRecordedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link AuditLogEntryRecordedEvent}.
 *
 * <p>Listens for audit log recording events to support traceability,
 * monitoring, or audit indexing side effects.</p>
 */
@Service
@Slf4j
public class AuditLogEntryRecordedEventHandler {

    /**
     * Default constructor.
     */
    public AuditLogEntryRecordedEventHandler() {
    }

    /**
     * Handles the {@link AuditLogEntryRecordedEvent}.
     *
     * @param event the {@link AuditLogEntryRecordedEvent} published by the RA bounded context
     */
    @EventListener(AuditLogEntryRecordedEvent.class)
    public void on(AuditLogEntryRecordedEvent event) {
        log.info(
                "Audit log entry recorded: Entry ID='{}', Action='{}', EntityType='{}', Entity ID='{}', PerformedBy='{}', OccurredAt='{}'.",
                event.auditLogEntryId(),
                event.action(),
                event.entityType(),
                event.entityId(),
                event.performedBy(),
                event.occurredAt()
        );

        // TODO: In the future, stream audit events to external observability services.
    }
}