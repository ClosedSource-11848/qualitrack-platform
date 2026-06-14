package com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources;

import java.math.BigDecimal;

/**
 * Resource representing a subscription plan exposed through the REST API.
 */
public record SubscriptionPlanResource(
        Long id,
        String code,
        String name,
        String description,
        BigDecimal amount,
        String currency,
        String billingCycle,
        String stripePriceId,
        Integer maxUsers,
        Integer maxEquipment,
        Boolean active
) {
}