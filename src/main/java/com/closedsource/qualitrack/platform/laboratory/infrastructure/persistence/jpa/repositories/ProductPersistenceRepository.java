package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories;

import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.entities.ProductPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPersistenceRepository extends JpaRepository<ProductPersistenceEntity, Long> {
    Optional<ProductPersistenceEntity> findByDomainId(String domainId);
    List<ProductPersistenceEntity> findAllByLaboratoryId(String laboratoryId);
    Optional<ProductPersistenceEntity> findByNameAndLaboratoryId(String name, String laboratoryId);
    boolean existsByDomainId(String domainId);
}