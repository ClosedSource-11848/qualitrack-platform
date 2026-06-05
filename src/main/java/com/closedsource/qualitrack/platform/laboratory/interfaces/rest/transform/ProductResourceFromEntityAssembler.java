package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.PharmaceuticalProduct;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.PharmaceuticalProductResource;

/**
 * Assembler to convert a PharmaceuticalProduct entity to a PharmaceuticalProductResource.
 */
public class ProductResourceFromEntityAssembler {
    /**
     * Converts a PharmaceuticalProduct entity to a PharmaceuticalProductResource.
     *
     * @param entity The {@link PharmaceuticalProduct} entity to convert.
     * @return The {@link PharmaceuticalProductResource} resource that results from the conversion.
     */
    public static PharmaceuticalProductResource toResourceFromEntity(PharmaceuticalProduct entity) {
        return new PharmaceuticalProductResource(
                entity.getId(),
                entity.getLaboratoryId(),
                entity.getName(),
                entity.getDescription(),
                entity.getActiveIngredient()
        );
    }
}