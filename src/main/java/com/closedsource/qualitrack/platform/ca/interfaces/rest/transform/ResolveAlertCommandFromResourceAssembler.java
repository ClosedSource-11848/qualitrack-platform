package com.closedsource.qualitrack.platform.ca.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.ca.domain.model.commands.ResolveAlertCommand;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.ResolveAlertResource;

/**
 * Assembler to convert ResolveAlertResource into ResolveAlertCommand.
 */
public class ResolveAlertCommandFromResourceAssembler {

    public static ResolveAlertCommand toCommandFromResource(Long alertId, ResolveAlertResource resource) {
        return new ResolveAlertCommand(
                alertId,
                resource.resolvedBy(),
                resource.resolutionNotes()
        );
    }
}