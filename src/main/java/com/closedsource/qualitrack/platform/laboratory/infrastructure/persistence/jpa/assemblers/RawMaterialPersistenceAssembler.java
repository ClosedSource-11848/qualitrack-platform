package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.RawMaterial;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.RawMaterialPersistenceEntity;

public final class RawMaterialPersistenceAssembler {

    private RawMaterialPersistenceAssembler() {}

    public static RawMaterial toDomainFromPersistence(RawMaterialPersistenceEntity entity) {
        if (entity == null) return null;

        return new RawMaterial(
                entity.getDomainId(),
                entity.getLaboratoryId(),
                entity.getName(),
                entity.getUnit(),
                entity.getStockQuantity()
        );
    }

    public static RawMaterialPersistenceEntity toPersistenceFromDomain(RawMaterial domain) {
        if (domain == null) return null;

        var entity = new RawMaterialPersistenceEntity();
        entity.setDomainId(domain.getId());
        entity.setLaboratoryId(domain.getLaboratoryId());
        entity.setName(domain.getName());
        entity.setUnit(domain.getUnit());
        entity.setStockQuantity(domain.getCurrentStock());

        entity.setMinimumThreshold(100);

        return entity;
    }
}