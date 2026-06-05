package com.closedsource.qualitrack.platform.laboratory.infrastructure.persistence.jpa.converters;

import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.Regulation;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts Regulation value objects between the domain model and persistence column values.
 */
@Converter(autoApply = false)
public class RegulationPersistenceConverter implements AttributeConverter<Regulation, String> {

    @Override
    public String convertToDatabaseColumn(Regulation attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public Regulation convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Regulation(dbData);
    }
}