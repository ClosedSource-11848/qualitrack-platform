package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities;

import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.converters.LaboratoryStatusPersistenceConverter;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.converters.RegulationPersistenceConverter;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.embeddables.AddressPersistenceEmbeddable;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryStatus;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.Regulation;
import com.closedsource.qualitrack.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "laboratories")
@Getter
@Setter
@NoArgsConstructor
public class LaboratoryPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "domain_id", nullable = false, unique = true, updatable = false)
    private String domainId;

    @Column(nullable = false)
    private String name;

    @Convert(converter = RegulationPersistenceConverter.class)
    @Column(nullable = false)
    private Regulation regulation;

    @Convert(converter = LaboratoryStatusPersistenceConverter.class)
    @Column(nullable = false)
    private LaboratoryStatus status;

    @Embedded
    private AddressPersistenceEmbeddable address;
}