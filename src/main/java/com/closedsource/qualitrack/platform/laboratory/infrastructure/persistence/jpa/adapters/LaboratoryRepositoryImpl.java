package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.adapters;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.Laboratory;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.LaboratoryRepository;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryName;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers.LaboratoryPersistenceAssembler;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories.LaboratoryPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LaboratoryRepositoryImpl implements LaboratoryRepository {

    private final LaboratoryPersistenceRepository repository;

    public LaboratoryRepositoryImpl(LaboratoryPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Laboratory> findById(String id) {
        return repository.findByDomainId(id).map(LaboratoryPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Laboratory> findByName(LaboratoryName name) {
        return repository.findByName(name.name()).map(LaboratoryPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Laboratory> findAll() {
        return repository.findAll().stream().map(LaboratoryPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Laboratory save(Laboratory laboratory) {
        var entityToSave = LaboratoryPersistenceAssembler.toPersistenceFromDomain(laboratory);

        repository.findByDomainId(laboratory.getId())
                .ifPresent(existing -> entityToSave.setId(existing.getId()));

        var saved = repository.save(entityToSave);
        return LaboratoryPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsByDomainId(id);
    }

    @Override
    public boolean existsByName(LaboratoryName name) {
        return repository.existsByName(name.name());
    }

    @Override
    public void deleteById(String id) {
        repository.findByDomainId(id).ifPresent(repository::delete);
    }
}