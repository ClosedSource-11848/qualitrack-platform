package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.adapters;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.PharmaceuticalProduct;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.ProductRepository;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.assemblers.ProductPersistenceAssembler;
import com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.repositories.ProductPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductPersistenceRepository repository;

    public ProductRepositoryImpl(ProductPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PharmaceuticalProduct> findById(String id) {
        return repository.findByDomainId(id).map(ProductPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<PharmaceuticalProduct> findAll() {
        return repository.findAll().stream().map(ProductPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<PharmaceuticalProduct> findAllByLaboratoryId(String laboratoryId) {
        return repository.findAllByLaboratoryId(laboratoryId).stream()
                .map(ProductPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<PharmaceuticalProduct> findByNameAndLaboratoryId(String name, String laboratoryId) {
        return repository.findByNameAndLaboratoryId(name, laboratoryId)
                .map(ProductPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public PharmaceuticalProduct save(PharmaceuticalProduct product) {
        var entityToSave = ProductPersistenceAssembler.toPersistenceFromDomain(product);
        repository.findByDomainId(product.getId())
                .ifPresent(existing -> entityToSave.setId(existing.getId()));

        var saved = repository.save(entityToSave);
        return ProductPersistenceAssembler.toDomainFromPersistence(saved);
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