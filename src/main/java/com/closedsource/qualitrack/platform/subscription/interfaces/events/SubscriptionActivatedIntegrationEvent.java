package com.closedsource.qualitrack.platform.subscription.interfaces.events;

import com.closedsource.qualitrack.platform.subscription.domain.model.events.SubscriptionActivatedEvent;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.BillingCycle;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.PlanCode;

/**
 * Integration event published when a subscription is activated.
 */
public record SubscriptionActivatedIntegrationEvent(
        Long subscriptionId,
        Long userId,
        Long laboratoryId,
        PlanCode planCode,
        BillingCycle billingCycle,
        String stripeCustomerId,
        String stripeSubscriptionId
) {
    public static SubscriptionActivatedIntegrationEvent from(SubscriptionActivatedEvent event) {
        return new SubscriptionActivatedIntegrationEvent(
                event.subscriptionId(),
                event.userId(),
                event.laboratoryId(),
                event.planCode(),
                event.billingCycle(),
                event.stripeCustomerId(),
                event.stripeSubscriptionId()
        );
    }
}