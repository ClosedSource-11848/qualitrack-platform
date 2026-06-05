package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.adapters;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.RawMaterial;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.RawMaterialRepository;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers.RawMaterialPersistenceAssembler;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories.RawMaterialPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RawMaterialRepositoryImpl implements RawMaterialRepository {

    private final RawMaterialPersistenceRepository repository;

    public RawMaterialRepositoryImpl(RawMaterialPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RawMaterial> findById(String id) {
        return repository.findByDomainId(id).map(RawMaterialPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<RawMaterial> findAll() {
        return repository.findAll().stream().map(RawMaterialPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<RawMaterial> findAllByLaboratoryId(String laboratoryId) {
        return repository.findAllByLaboratoryId(laboratoryId).stream()
                .map(RawMaterialPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public RawMaterial save(RawMaterial rawMaterial) {
        var entityToSave = RawMaterialPersistenceAssembler.toPersistenceFromDomain(rawMaterial);
        repository.findByDomainId(rawMaterial.getId())
                .ifPresent(existing -> entityToSave.setId(existing.getId()));

        var saved = repository.save(entityToSave);
        return RawMaterialPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsByDomainId(id);
    }

    @Override
    public void deleteById(String id) {
        repository.findByDomainId(id).ifPresent(repository::delete);
    }
}