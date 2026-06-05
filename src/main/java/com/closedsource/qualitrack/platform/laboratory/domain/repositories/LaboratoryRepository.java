package com.closedsource.qualitrack.platform.laboratory.domain.repositories;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.Laboratory;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryName;

import java.util.List;
import java.util.Optional;

/**
 * Laboratory repository port.
 */
public interface LaboratoryRepository {
    Optional<Laboratory> findById(String id);

    Optional<Laboratory> findByName(LaboratoryName name);

    List<Laboratory> findAll();

    Laboratory save(Laboratory laboratory);

    boolean existsById(String id);

    boolean existsByName(LaboratoryName name);

    void deleteById(String id);
}