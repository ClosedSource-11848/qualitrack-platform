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
     * @param laboratoryId The laboratory numeric ID extracted from the path.
     * @param resource The {@link UpdateLaboratoryResource} resource to convert.
     * @return The {@link UpdateLaboratoryCommand} command that results from the conversion.
     */
    public static UpdateLaboratoryCommand toCommandFromResource(Long laboratoryId, UpdateLaboratoryResource resource) {
        return new UpdateLaboratoryCommand(
                laboratoryId,
                resource.name(),
                resource.phone(),
                resource.address(),
                resource.applicableRegulations()
                );
    }
}