package com.closedsource.qualitrack.platform.equipment.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.equipment.domain.model.commands.RegisterMaintenanceCommand;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.RegisterMaintenanceResource;

/**
 * Assembler to convert a RegisterMaintenanceResource to a RegisterMaintenanceCommand.
 */
public class RegisterMaintenanceCommandFromResourceAssembler {

    /**
     * Converts a RegisterMaintenanceResource to a RegisterMaintenanceCommand.
     *
     * @param resource The {@link RegisterMaintenanceResource} resource to convert.
     * @return The {@link RegisterMaintenanceCommand} command that results from the conversion.
     */
    public static RegisterMaintenanceCommand toCommandFromResource(RegisterMaintenanceResource resource) {
        return new RegisterMaintenanceCommand(
                resource.equipmentId(),
                resource.maintenanceDate(),
                resource.technicianName(),
                resource.description(),
                resource.type()
        );
    }
}