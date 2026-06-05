package com.closedsource.qualitrack.platform.laboratory.domain.repositories;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.RawMaterial;

import java.util.List;
import java.util.Optional;

/**
 * Raw material repository port.
 */
public interface RawMaterialRepository {
    Optional<RawMaterial> findById(String id);

    List<RawMaterial> findAll();

    List<RawMaterial> findAllByLaboratoryId(String laboratoryId);

    RawMaterial save(RawMaterial rawMaterial);

    boolean existsById(String id);

    void deleteById(String id);
}