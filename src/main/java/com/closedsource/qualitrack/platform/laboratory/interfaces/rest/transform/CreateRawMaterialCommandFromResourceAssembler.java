package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateRawMaterialCommand;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.CreateRawMaterialResource;

/**
 * Assembler to convert a CreateRawMaterialResource to a CreateRawMaterialCommand.
 */
public class CreateRawMaterialCommandFromResourceAssembler {

    public static CreateRawMaterialCommand toCommandFromResource(Long laboratoryId, CreateRawMaterialResource resource) {
        return new CreateRawMaterialCommand(
                laboratoryId,
                resource.name(),
                resource.code(),
                resource.supplier(),
                resource.batchNumber(),
                resource.expirationDate(),
                resource.quantityInStock(),
                resource.unit(),
                resource.minimumStock()
        );
    }
}