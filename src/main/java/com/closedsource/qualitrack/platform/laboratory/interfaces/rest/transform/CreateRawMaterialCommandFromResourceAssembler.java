package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateRawMaterialCommand;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.CreateRawMaterialResource;

/**
 * Assembler to convert a CreateRawMaterialResource to a CreateRawMaterialCommand.
 */
public class CreateRawMaterialCommandFromResourceAssembler {
    /**
     * Converts a CreateRawMaterialResource to a CreateRawMaterialCommand.
     *
     * @param resource The {@link CreateRawMaterialResource} resource to convert.
     * @return The {@link CreateRawMaterialCommand} command that results from the conversion.
     */
    public static CreateRawMaterialCommand toCommandFromResource(CreateRawMaterialResource resource) {
        return new CreateRawMaterialCommand(
                resource.laboratoryId(),
                resource.name(),
                resource.unit(),
                resource.initialStock()
        );
    }
}