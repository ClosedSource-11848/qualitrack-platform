package com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects;

/**
 * Represents the lifecycle status of a subscription.
 */
public enum SubscriptionStatus {
    ACTIVE,
    INACTIVE,
    PENDING_PAYMENT,
    CANCELLED,
    EXPIRED
}