package com.closedsource.qualitrack.platform.ra.application.commandservices;

import com.closedsource.qualitrack.platform.ra.domain.model.commands.RecordAuditLogEntryCommand;
import com.closedsource.qualitrack.platform.ra.domain.model.entities.AuditLogEntry;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;

/**
 * Application service contract for audit log write operations.
 */
public interface AuditLogCommandService {

    /**
     * Handles the recording of a new audit log entry.
     *
     * @param command command containing audit action and actor context
     * @return recorded audit log entry or an application error
     * @see RecordAuditLogEntryCommand
     */
    Result<AuditLogEntry, ApplicationError> handle(RecordAuditLogEntryCommand command);
}