package com.closedsource.qualitrack.platform.ra.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.ra.domain.model.commands.GenerateBatchReportCommand;
import com.closedsource.qualitrack.platform.ra.interfaces.rest.resources.GenerateBatchReportResource;

/**
 * Assembler that transforms batch report REST resources into application commands.
 */
public final class GenerateBatchReportCommandFromResourceAssembler {
    private GenerateBatchReportCommandFromResourceAssembler() {
    }

    /**
     * Converts a batch report resource into a command.
     *
     * @param resource the batch report request resource
     * @return the batch report generation command
     */
    public static GenerateBatchReportCommand toCommandFromResource(
            GenerateBatchReportResource resource
    ) {
        return new GenerateBatchReportCommand(
                resource.batchId(),
                resource.includeTelemetry(),
                resource.includeDeviations(),
                resource.format(),
                resource.requestedBy()
        );
    }
}