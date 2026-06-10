package com.closedsource.qualitrack.platform.ca.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.ca.domain.model.commands.UpdateNotificationPreferenceCommand;
import com.closedsource.qualitrack.platform.ca.interfaces.rest.resources.UpdateNotificationPreferenceResource;

/**
 * Assembler to convert UpdateNotificationPreferenceResource into UpdateNotificationPreferenceCommand.
 */
public class UpdateNotificationPreferenceCommandFromResourceAssembler {

    public static UpdateNotificationPreferenceCommand toCommandFromResource(
            Long userId,
            UpdateNotificationPreferenceResource resource
    ) {
        return new UpdateNotificationPreferenceCommand(
                userId,
                resource.emailEnabled(),
                resource.smsEnabled(),
                resource.inAppEnabled(),
                resource.minimumSeverity()
        );
    }
}