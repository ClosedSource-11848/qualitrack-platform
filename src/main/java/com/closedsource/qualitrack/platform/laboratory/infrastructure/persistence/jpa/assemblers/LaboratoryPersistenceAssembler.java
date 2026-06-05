package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.Laboratory;
import com.closedsource.qualitrack.platform.laboratory.domain.model.entities.LaboratoryAddress;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryName;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.embeddables.AddressPersistenceEmbeddable;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.LaboratoryPersistenceEntity;

public final class LaboratoryPersistenceAssembler {

    private LaboratoryPersistenceAssembler() {}

    public static Laboratory toDomainFromPersistence(LaboratoryPersistenceEntity entity) {
        if (entity == null) return null;

        var address = new LaboratoryAddress(
                entity.getAddress().getStreet(),
                entity.getAddress().getCity(),
                entity.getAddress().getZipCode()
        );

        return new Laboratory(
                entity.getDomainId(),
                new LaboratoryName(entity.getName()),
                entity.getRegulation(),
                entity.getStatus(),
                address
        );
    }

    public static LaboratoryPersistenceEntity toPersistenceFromDomain(Laboratory domain) {
        if (domain == null) return null;

        var entity = new LaboratoryPersistenceEntity();
        entity.setDomainId(domain.getId());
        entity.setName(domain.getName().name());
        entity.setRegulation(domain.getRegulation());
        entity.setStatus(domain.getStatus());

        entity.setAddress(new AddressPersistenceEmbeddable(
                domain.getAddress().getStreet(),
                domain.getAddress().getCity(),
                domain.getAddress().getZipCode()
        ));

        return entity;
    }
}