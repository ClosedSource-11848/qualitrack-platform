package com.closedsource.qualitrack.platform.laboratory.domain.repositories;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.StaffMember;

import java.util.List;
import java.util.Optional;

/**
 * Staff member repository port.
 */
public interface StaffRepository {
    Optional<StaffMember> findById(String id);

    List<StaffMember> findAll();

    List<StaffMember> findAllByLaboratoryId(String laboratoryId);

    StaffMember save(StaffMember staffMember);

    boolean existsById(String id);

    void deleteById(String id);
}