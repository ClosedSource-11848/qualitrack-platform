package com.closedsource.qualitrack.platform.equipment.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.equipment.domain.model.commands.ConfigureBpmParametersCommand;
import com.closedsource.qualitrack.platform.equipment.interfaces.rest.resources.ConfigureBpmResource;

/**
 * Assembler to convert a ConfigureBpmResource to a ConfigureBpmParametersCommand.
 */
public class ConfigureBpmCommandFromResourceAssembler {

    /**
     * Converts a ConfigureBpmResource to a ConfigureBpmParametersCommand.
     *
     * @param resource The {@link ConfigureBpmResource} resource to convert.
     * @return The {@link ConfigureBpmParametersCommand} command that results from the conversion.
     */
    public static ConfigureBpmParametersCommand toCommandFromResource(ConfigureBpmResource resource) {
        return new ConfigureBpmParametersCommand(
                resource.equipmentId(),
                resource.parameterName(),
                resource.minValue(),
                resource.maxValue(),
                resource.unit()
        );
    }
}