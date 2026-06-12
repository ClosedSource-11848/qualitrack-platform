package com.closedsource.qualitrack.platform.ra.interfaces.rest.resources;

import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;

/**
 * Resource used to request recording of an audit log entry.
 *
 * @param action The performed audit action.
 * @param entityType The affected entity type.
 * @param entityId The affected entity identifier, if applicable.
 * @param performedBy The numeric identifier of the actor.
 * @param details Additional action details.
 */
public record RecordAuditLogEntryResource(
        AuditAction action,
        String entityType,
        Long entityId,
        Long performedBy,
        String details
) {
}