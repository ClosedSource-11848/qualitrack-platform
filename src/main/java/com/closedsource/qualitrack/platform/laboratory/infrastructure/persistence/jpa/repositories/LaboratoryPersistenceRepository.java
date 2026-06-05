package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories;

import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.LaboratoryPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaboratoryPersistenceRepository extends JpaRepository<LaboratoryPersistenceEntity, Long> {
    Optional<LaboratoryPersistenceEntity> findByDomainId(String domainId);
    Optional<LaboratoryPersistenceEntity> findByName(String name);
    boolean existsByDomainId(String domainId);
    boolean existsByName(String name);
}