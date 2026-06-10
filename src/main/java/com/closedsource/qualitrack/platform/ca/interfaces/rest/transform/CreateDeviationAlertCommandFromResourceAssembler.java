package com.closedsource.qualitrack.platform.ca.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.ca.domain.model.commands.CreateDeviationAlertCommand;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.CreateDeviationAlertResource;

/**
 * Assembler to convert CreateDeviationAlertResource into CreateDeviationAlertCommand.
 */
public class CreateDeviationAlertCommandFromResourceAssembler {

    public static CreateDeviationAlertCommand toCommandFromResource(CreateDeviationAlertResource resource) {
        return new CreateDeviationAlertCommand(
                resource.equipmentId(),
                resource.batchId(),
                resource.parameterName(),
                resource.recordedValue(),
                resource.thresholdValue(),
                resource.unit(),
                resource.timestamp(),
                resource.severity()
        );
    }
}