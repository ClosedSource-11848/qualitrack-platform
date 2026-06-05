package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.StaffMember;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.StaffPersistenceEntity;

public final class StaffPersistenceAssembler {

    private StaffPersistenceAssembler() {}

    public static StaffMember toDomainFromPersistence(StaffPersistenceEntity entity) {
        if (entity == null) return null;

        return new StaffMember(
                entity.getDomainId(),
                entity.getLaboratoryId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getRole(),
                entity.isActive()
        );
    }

    public static StaffPersistenceEntity toPersistenceFromDomain(StaffMember domain) {
        if (domain == null) return null;

        var entity = new StaffPersistenceEntity();
        entity.setDomainId(domain.getId());
        entity.setLaboratoryId(domain.getLaboratoryId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setRole(domain.getRole());
        entity.setActive(domain.isActive());

        return entity;
    }
}