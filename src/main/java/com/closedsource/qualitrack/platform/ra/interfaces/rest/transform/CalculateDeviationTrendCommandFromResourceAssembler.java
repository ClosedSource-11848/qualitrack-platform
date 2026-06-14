package com.closedsource.qualitrack.platform.ra.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.ra.domain.model.commands.CalculateDeviationTrendCommand;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.CalculateDeviationTrendResource;

/**
 * Assembler that transforms deviation trend calculation resources into commands.
 */
public final class CalculateDeviationTrendCommandFromResourceAssembler {

    /**
     * Private constructor to prevent instantiation.
     */
    private CalculateDeviationTrendCommandFromResourceAssembler() {
    }

    /**
     * Converts a deviation trend calculation resource into a command.
     *
     * @param resource The request resource.
     * @return The calculation command.
     */
    public static CalculateDeviationTrendCommand toCommandFromResource(
            CalculateDeviationTrendResource resource
    ) {
        return new CalculateDeviationTrendCommand(
                resource.equipmentId(),
                resource.parameterName()
        );
    }
}