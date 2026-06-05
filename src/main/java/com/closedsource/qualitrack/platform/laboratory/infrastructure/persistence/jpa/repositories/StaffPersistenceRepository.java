package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories;

import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.StaffPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffPersistenceRepository extends JpaRepository<StaffPersistenceEntity, Long> {
    Optional<StaffPersistenceEntity> findByDomainId(String domainId);
    List<StaffPersistenceEntity> findAllByLaboratoryId(String laboratoryId);
    boolean existsByDomainId(String domainId);
}