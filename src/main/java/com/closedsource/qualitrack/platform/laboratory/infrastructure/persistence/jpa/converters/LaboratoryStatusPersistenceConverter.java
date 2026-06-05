package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.converters;

import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts LaboratoryStatus enum between the domain model and persistence column values.
 */
@Converter(autoApply = false)
public class LaboratoryStatusPersistenceConverter implements AttributeConverter<LaboratoryStatus, String> {

    @Override
    public String convertToDatabaseColumn(LaboratoryStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public LaboratoryStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LaboratoryStatus.valueOf(dbData);
    }
}