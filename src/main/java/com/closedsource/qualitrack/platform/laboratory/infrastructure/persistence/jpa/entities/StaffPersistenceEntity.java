package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities;

import com.closedsource.qualitrack.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "staff_members")
@Getter
@Setter
@NoArgsConstructor
public class StaffPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "domain_id", nullable = false, unique = true, updatable = false)
    private String domainId;

    @Column(name = "laboratory_id", nullable = false)
    private String laboratoryId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean active;
}