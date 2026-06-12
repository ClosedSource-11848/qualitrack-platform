package com.closedsource.qualitrack.platform.ra.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.ra.domain.model.commands.RecordAuditLogEntryCommand;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.RecordAuditLogEntryResource;

/**
 * Assembler that transforms audit log REST resources into application commands.
 */
public final class RecordAuditLogEntryCommandFromResourceAssembler {
    private RecordAuditLogEntryCommandFromResourceAssembler() {
    }

    /**
     * Converts an audit log entry resource into a command.
     *
     * @param resource the audit log entry request resource
     * @return the audit log entry recording command
     */
    public static RecordAuditLogEntryCommand toCommandFromResource(
            RecordAuditLogEntryResource resource
    ) {
        return new RecordAuditLogEntryCommand(
                resource.action(),
                resource.entityType(),
                resource.entityId(),
                resource.performedBy(),
                resource.details()
        );
    }
}