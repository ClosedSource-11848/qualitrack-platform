package com.closedsource.qualitrack.platform.tracking.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.tracking.domain.model.commands.RecordTelemetryHistoryPointCommand;
import com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources.RecordTelemetryHistoryPointResource;

/**
 * Assembler that transforms telemetry history point REST resources into application commands.
 */
public final class RecordTelemetryHistoryPointCommandFromResourceAssembler {
    private RecordTelemetryHistoryPointCommandFromResourceAssembler() {
    }

    /**
     * Converts a record telemetry history point resource into a command.
     *
     * @param resource the record telemetry history point resource
     * @return the record telemetry history point command
     */
    public static RecordTelemetryHistoryPointCommand toCommandFromResource(
            RecordTelemetryHistoryPointResource resource
    ) {
        return new RecordTelemetryHistoryPointCommand(
                resource.equipmentId(),
                resource.parameterName(),
                resource.recordedValue(),
                resource.timestamp(),
                resource.isAnomaly()
        );
    }
}