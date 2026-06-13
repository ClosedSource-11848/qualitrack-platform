package com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.tracking.domain.model.commands.RecordMeasurementCommand;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.RecordMeasurementResource;

/**
 * Assembler that transforms measurement REST resources into application commands.
 */
public final class RecordMeasurementCommandFromResourceAssembler {
    private RecordMeasurementCommandFromResourceAssembler() {
    }

    /**
     * Converts a record measurement resource into a command.
     *
     * @param resource the record measurement resource
     * @return the record measurement command
     */
    public static RecordMeasurementCommand toCommandFromResource(RecordMeasurementResource resource) {
        return new RecordMeasurementCommand(
                resource.equipmentId(),
                resource.parameterName(),
                resource.value(),
                resource.unit(),
                resource.timestamp()
        );
    }
}