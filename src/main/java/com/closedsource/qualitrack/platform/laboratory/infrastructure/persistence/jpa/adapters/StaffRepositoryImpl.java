package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.adapters;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.StaffMember;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.StaffRepository;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers.StaffPersistenceAssembler;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories.StaffPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StaffRepositoryImpl implements StaffRepository {

    private final StaffPersistenceRepository repository;

    public StaffRepositoryImpl(StaffPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<StaffMember> findById(String id) {
        return repository.findByDomainId(id).map(StaffPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<StaffMember> findAll() {
        return repository.findAll().stream().map(StaffPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<StaffMember> findAllByLaboratoryId(String laboratoryId) {
        return repository.findAllByLaboratoryId(laboratoryId).stream()
                .map(StaffPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public StaffMember save(StaffMember staffMember) {
        var entityToSave = StaffPersistenceAssembler.toPersistenceFromDomain(staffMember);
        repository.findByDomainId(staffMember.getId())
                .ifPresent(existing -> entityToSave.setId(existing.getId()));

        var saved = repository.save(entityToSave);
        return StaffPersistenceAssembler.toDomainFromPersistence(saved);
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