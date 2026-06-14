package com.closedsource.qualitrack.platform.subscription.domain.model.aggregates;

import com.closedsource.qualitrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.closedsource.qualitrack.platform.subscription.domain.model.commands.ActivateSubscriptionCommand;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.BillingCycle;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.PlanCode;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import lombok.Getter;

import java.util.Objects;

/**
 * Subscription aggregate root.
 *
 * <p>Represents the active commercial relationship between a laboratory and
 * QualiTrack, backed by Stripe as the payment provider.</p>
 */
@Getter
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {

    private Long id;
    private Long userId;
    private Long laboratoryId;
    private PlanCode planCode;
    private BillingCycle billingCycle;
    private SubscriptionStatus status;
    private String stripeCustomerId;
    private String stripeSubscriptionId;
    private String stripeCheckoutSessionId;
    private String currentPeriodStart;
    private String currentPeriodEnd;
    private String cancelledAt;
    private Long cancelledBy;

    /**
     * Default constructor.
     */
    public Subscription() {
        // Required for reconstruction
    }

    /**
     * Reconstructs a subscription aggregate.
     */
    public Subscription(
            Long id,
            Long userId,
            Long laboratoryId,
            PlanCode planCode,
            BillingCycle billingCycle,
            SubscriptionStatus status,
            String stripeCustomerId,
            String stripeSubscriptionId,
            String stripeCheckoutSessionId,
            String currentPeriodStart,
            String currentPeriodEnd,
            String cancelledAt,
            Long cancelledBy
    ) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId, "User id is required");
        this.laboratoryId = Objects.requireNonNull(laboratoryId, "Laboratory id is required");
        this.planCode = Objects.requireNonNull(planCode, "Plan code is required");
        this.billingCycle = Objects.requireNonNull(billingCycle, "Billing cycle is required");
        this.status = Objects.requireNonNull(status, "Subscription status is required");
        this.stripeCustomerId = stripeCustomerId;
        this.stripeSubscriptionId = stripeSubscriptionId;
        this.stripeCheckoutSessionId = stripeCheckoutSessionId;
        this.currentPeriodStart = currentPeriodStart;
        this.currentPeriodEnd = currentPeriodEnd;
        this.cancelledAt = cancelledAt;
        this.cancelledBy = cancelledBy;
    }

    /**
     * Creates an activated subscription from a Stripe activation command.
     *
     * @param command The activation command.
     * @return A new active subscription.
     */
    public static Subscription activate(ActivateSubscriptionCommand command) {
        return new Subscription(
                null,
                command.userId(),
                command.laboratoryId(),
                command.planCode(),
                command.billingCycle(),
                SubscriptionStatus.ACTIVE,
                command.stripeCustomerId(),
                command.stripeSubscriptionId(),
                command.stripeCheckoutSessionId(),
                command.currentPeriodStart(),
                command.currentPeriodEnd(),
                null,
                null
        );
    }

    /**
     * Cancels the subscription.
     *
     * @param cancelledBy The user requesting cancellation.
     * @param cancelledAt The cancellation timestamp.
     */
    public void cancel(Long cancelledBy, String cancelledAt) {
        if (!SubscriptionStatus.ACTIVE.equals(this.status)) {
            throw new IllegalStateException("Only active subscriptions can be cancelled");
        }
        if (cancelledBy == null || cancelledBy <= 0) {
            throw new IllegalArgumentException("Cancelled by must be a positive user id");
        }
        if (cancelledAt == null || cancelledAt.isBlank()) {
            throw new IllegalArgumentException("Cancelled at is required");
        }

        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledBy = cancelledBy;
        this.cancelledAt = cancelledAt;
    }

    /**
     * Indicates whether the subscription is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return SubscriptionStatus.ACTIVE.equals(status);
    }
}