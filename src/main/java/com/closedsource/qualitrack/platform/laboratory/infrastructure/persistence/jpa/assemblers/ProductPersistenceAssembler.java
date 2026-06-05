package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.PharmaceuticalProduct;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.ProductPersistenceEntity;

public final class ProductPersistenceAssembler {

    private ProductPersistenceAssembler() {}

    public static PharmaceuticalProduct toDomainFromPersistence(ProductPersistenceEntity entity) {
        if (entity == null) return null;

        return new PharmaceuticalProduct(
                entity.getDomainId(),
                entity.getLaboratoryId(),
                entity.getName(),
                entity.getDescription(),
                entity.getActiveIngredient()
        );
    }

    public static ProductPersistenceEntity toPersistenceFromDomain(PharmaceuticalProduct domain) {
        if (domain == null) return null;

        var entity = new ProductPersistenceEntity();
        entity.setDomainId(domain.getId());
        entity.setLaboratoryId(domain.getLaboratoryId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setActiveIngredient(domain.getActiveIngredient());

        return entity;
    }
}