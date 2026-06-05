package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories;

import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.RawMaterialPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RawMaterialPersistenceRepository extends JpaRepository<RawMaterialPersistenceEntity, Long> {
    Optional<RawMaterialPersistenceEntity> findByDomainId(String domainId);
    List<RawMaterialPersistenceEntity> findAllByLaboratoryId(String laboratoryId);
    boolean existsByDomainId(String domainId);
}