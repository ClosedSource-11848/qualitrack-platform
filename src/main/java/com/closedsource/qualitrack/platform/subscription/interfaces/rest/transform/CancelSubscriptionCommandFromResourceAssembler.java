package com.closedsource.qualitrack.platform.subscription.interfaces.rest.transform;

import com.closedsource.qualitrack.platform.subscription.domain.model.commands.CancelSubscriptionCommand;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.CancelSubscriptionResource;

/**
 * Assembler that maps REST cancellation resources to domain commands.
 */
public class CancelSubscriptionCommandFromResourceAssembler {

    public static CancelSubscriptionCommand toCommandFromResource(
            Long subscriptionId,
            CancelSubscriptionResource resource
    ) {
        return new CancelSubscriptionCommand(
                subscriptionId,
                resource.cancelledBy()
        );
    }
}