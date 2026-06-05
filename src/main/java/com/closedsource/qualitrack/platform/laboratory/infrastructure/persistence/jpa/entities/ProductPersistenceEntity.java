package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities;

import com.closedsource.qualitrack.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pharmaceutical_products")
@Getter
@Setter
@NoArgsConstructor
public class ProductPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "domain_id", nullable = false, unique = true, updatable = false)
    private String domainId;

    @Column(name = "laboratory_id", nullable = false)
    private String laboratoryId;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "active_ingredient", nullable = false)
    private String activeIngredient;
}