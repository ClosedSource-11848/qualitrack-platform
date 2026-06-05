package com.closedsource.qualitrack.platform.laboratory.domain.repositories;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.PharmaceuticalProduct;

import java.util.List;
import java.util.Optional;

/**
 * Pharmaceutical product repository port.
 */
public interface ProductRepository {
    Optional<PharmaceuticalProduct> findById(String id);

    List<PharmaceuticalProduct> findAll();

    List<PharmaceuticalProduct> findAllByLaboratoryId(String laboratoryId);

    Optional<PharmaceuticalProduct> findByNameAndLaboratoryId(String name, String laboratoryId);

    PharmaceuticalProduct save(PharmaceuticalProduct product);

    boolean existsById(String id);

    void deleteById(String id);
}