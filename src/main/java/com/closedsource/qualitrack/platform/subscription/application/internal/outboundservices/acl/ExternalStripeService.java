package com.closedsource.qualitrack.platform.subscription.application.internal.outboundservices.acl;

import com.closedsource.qualitrack.platform.subscription.domain.exceptions.StripeCheckoutSessionCreationException;
import com.closedsource.qualitrack.platform.subscription.domain.model.commands.CreateCheckoutSessionCommand;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;

/**
 * ACL service used by the Subscription bounded context to interact with Stripe.
 *
 * <p>This service isolates Stripe SDK details from the subscription domain and
 * application services.</p>
 */
@Service
public class ExternalStripeService {

    private final String stripeSecretKey;

    public ExternalStripeService(@Value("${stripe.secret-key}") String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
    }

    /**
     * Creates a real Stripe Checkout Session for a subscription plan.
     *
     * @param command checkout command containing user, laboratory, plan and redirect data
     * @param stripePriceId Stripe Price ID configured for the selected plan
     * @return checkout session identifier and hosted checkout URL
     */
    public StripeCheckoutSessionResult createCheckoutSession(
            CreateCheckoutSessionCommand command,
            String stripePriceId
    ) {
        try {
            var params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl(appendCheckoutSessionPlaceholder(command.successUrl()))
                    .setCancelUrl(command.cancelUrl())
                    .setClientReferenceId(String.valueOf(command.laboratoryId()))
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(stripePriceId)
                                    .setQuantity(1L)
                                    .build()
                    )
                    .putMetadata("userId", String.valueOf(command.userId()))
                    .putMetadata("laboratoryId", String.valueOf(command.laboratoryId()))
                    .putMetadata("planCode", command.planCode().name())
                    .putMetadata("billingCycle", command.billingCycle().name())
                    .build();

            var session = Session.create(params, requestOptions());

            return new StripeCheckoutSessionResult(session.getId(), session.getUrl());

        } catch (StripeException e) {
            throw new StripeCheckoutSessionCreationException(e.getMessage());
        }
    }

    /**
     * Retrieves Stripe subscription details needed to activate a local subscription.
     *
     * @param stripeSubscriptionId Stripe subscription identifier
     * @return Stripe subscription data used by the subscription BC
     */
    public StripeSubscriptionDetails retrieveSubscriptionDetails(String stripeSubscriptionId) {
        try {
            var subscription = Subscription.retrieve(stripeSubscriptionId, requestOptions());

            var periodStart = getCurrentPeriodStart(subscription);
            var periodEnd = getCurrentPeriodEnd(subscription);

            return new StripeSubscriptionDetails(
                    subscription.getCustomer(),
                    subscription.getId(),
                    toIsoDateTime(periodStart),
                    toIsoDateTime(periodEnd)
            );

        } catch (StripeException e) {
            throw new IllegalStateException("Failed to retrieve Stripe subscription: " + e.getMessage(), e);
        }
    }

    /**
     * Cancels an active Stripe subscription.
     *
     * @param stripeSubscriptionId Stripe subscription identifier
     */
    public void cancelSubscription(String stripeSubscriptionId) {
        try {
            var subscription = Subscription.retrieve(stripeSubscriptionId, requestOptions());
            subscription.cancel(SubscriptionCancelParams.builder().build(), requestOptions());
        } catch (StripeException e) {
            throw new IllegalStateException("Failed to cancel Stripe subscription: " + e.getMessage(), e);
        }
    }

    private RequestOptions requestOptions() {
        return RequestOptions.builder()
                .setApiKey(stripeSecretKey)
                .build();
    }

    private String appendCheckoutSessionPlaceholder(String successUrl) {
        if (successUrl.contains("{CHECKOUT_SESSION_ID}")) {
            return successUrl;
        }

        var separator = successUrl.contains("?") ? "&" : "?";
        return successUrl + separator + "session_id={CHECKOUT_SESSION_ID}";
    }

    private Long getCurrentPeriodStart(Subscription subscription) {
        if (subscription.getItems() == null
                || subscription.getItems().getData() == null
                || subscription.getItems().getData().isEmpty()) {
            return null;
        }

        return subscription.getItems().getData().getFirst().getCurrentPeriodStart();
    }

    private Long getCurrentPeriodEnd(Subscription subscription) {
        if (subscription.getItems() == null
                || subscription.getItems().getData() == null
                || subscription.getItems().getData().isEmpty()) {
            return null;
        }

        return subscription.getItems().getData().getFirst().getCurrentPeriodEnd();
    }

    private String toIsoDateTime(Long epochSeconds) {
        if (epochSeconds == null) return null;

        return Instant.ofEpochSecond(epochSeconds)
                .atOffset(ZoneOffset.UTC)
                .toString();
    }
}