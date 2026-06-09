package com.closedsource.qualitrack.platform.batch.infrastructure.persistence.jpa.assemblers;

import com.closedsource.qualitrack.platform.batch.domain.model.entities.RawMaterialUsage;
import com.closedsource.qualitrack.platform.batch.infrastructure.persistence.jpa.entities.RawMaterialUsagePersistenceEntity;

/**
 * Static assembler between raw material usage domain and persistence representations.
 */
public final class RawMaterialUsagePersistenceAssembler {

    private RawMaterialUsagePersistenceAssembler() {
    }

    public static RawMaterialUsage toDomainFromPersistence(RawMaterialUsagePersistenceEntity entity) {
        if (entity == null) return null;

        return new RawMaterialUsage(
                entity.getId(),
                entity.getBatchId(),
                entity.getRawMaterialId(),
                entity.getRawMaterialName(),
                entity.getQuantityUsed(),
                entity.getUnit(),
                entity.getUsageDate()
        );
    }

    public static RawMaterialUsagePersistenceEntity toPersistenceFromDomain(RawMaterialUsage usage) {
        if (usage == null) return null;

        var entity = new RawMaterialUsagePersistenceEntity();

        if (usage.getId() != null) {
            entity.setId(usage.getId());
        }

        entity.setBatchId(usage.getBatchId());
        entity.setRawMaterialId(usage.getRawMaterialId());
        entity.setRawMaterialName(usage.getRawMaterialName());
        entity.setQuantityUsed(usage.getQuantityUsed());
        entity.setUnit(usage.getUnit());
        entity.setUsageDate(usage.getUsageDate());

        return entity;
    }
}