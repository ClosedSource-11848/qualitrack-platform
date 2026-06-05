package com.closedsource.qualitrack.platform.laboratory.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.UpdateLaboratoryCommand;
import com.closedsource.qualitrack.platform.laboratory.interfaces.rest.resources.UpdateLaboratoryResource;

/**
 * Assembler to convert an UpdateLaboratoryResource to an UpdateLaboratoryCommand.
 */
public class UpdateLaboratoryCommandFromResourceAssembler {
    /**
     * Converts an UpdateLaboratoryResource to an UpdateLaboratoryCommand.
     *
     * @param laboratoryId The laboratory ID.
     * @param resource The {@link UpdateLaboratoryResource} resource to convert.
     * @return The {@link UpdateLaboratoryCommand} command that results from the conversion.
     */
    public static UpdateLaboratoryCommand toCommandFromResource(String laboratoryId, UpdateLaboratoryResource resource) {
        return new UpdateLaboratoryCommand(
                laboratoryId,
                resource.name(),
                resource.regulationCode()
        );
    }
}