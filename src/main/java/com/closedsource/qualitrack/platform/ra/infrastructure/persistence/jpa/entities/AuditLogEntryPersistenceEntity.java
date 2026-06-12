package com.closedsource.qualitrack.platform.ra.infrastructure.persistence.jpa.entities;

import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;
import com.closedsource.qualitrack.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity representing an immutable audit log entry.
 */
@Entity
@Table(name = "audit_log_entries")
@Getter
@Setter
@NoArgsConstructor
public class AuditLogEntryPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * Converted automatically by AuditActionPersistenceConverter.
     */
    @Column(nullable = false, length = 50)
    private AuditAction action;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "performed_by", nullable = false)
    private Long performedBy;

    @Column(nullable = false, length = 30)
    private String timestamp;

    @Column(length = 1000)
    private String details;
}