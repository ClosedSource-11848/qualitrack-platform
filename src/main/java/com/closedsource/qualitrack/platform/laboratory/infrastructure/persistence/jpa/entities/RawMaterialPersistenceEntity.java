package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities;

import com.closedsource.qualitrack.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "raw_materials")
@Getter
@Setter
@NoArgsConstructor
public class RawMaterialPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "domain_id", nullable = false, unique = true, updatable = false)
    private String domainId;

    @Column(name = "laboratory_id", nullable = false)
    private String laboratoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String unit;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "minimum_threshold", nullable = false)
    private Integer minimumThreshold;
}