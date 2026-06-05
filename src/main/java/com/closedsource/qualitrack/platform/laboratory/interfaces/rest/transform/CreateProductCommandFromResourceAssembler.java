package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateProductCommand;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.CreateProductResource;

/**
 * Assembler to convert a CreateProductResource to a CreateProductCommand.
 */
public class CreateProductCommandFromResourceAssembler {
    /**
     * Converts a CreateProductResource to a CreateProductCommand.
     *
     * @param resource The {@link CreateProductResource} resource to convert.
     * @return The {@link CreateProductCommand} command that results from the conversion.
     */
    public static CreateProductCommand toCommandFromResource(CreateProductResource resource) {
        return new CreateProductCommand(
                resource.laboratoryId(),
                resource.name(),
                resource.description(),
                resource.activeIngredient()
        );
    }
}