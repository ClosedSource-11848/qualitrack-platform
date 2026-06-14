package com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources;

/**
 * Request resource used to cancel an active subscription.
 */
public record CancelSubscriptionResource(
        Long cancelledBy
) {
}