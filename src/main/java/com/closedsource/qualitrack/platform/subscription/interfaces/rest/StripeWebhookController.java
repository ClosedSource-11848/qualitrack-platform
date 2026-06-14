package com.closedsource.qualitrack.platform.subscription.interfaces.rest;

import com.closedsource.qualitrack.platform.subscription.application.commandservices.SubscriptionCommandService;
import com.closedsource.qualitrack.platform.subscription.application.internal.outboundservices.acl.ExternalStripeService;
import com.closedsource.qualitrack.platform.subscription.application.queryservices.SubscriptionQueryService;
import com.closedsource.qualitrack.platform.subscription.domain.model.commands.ActivateSubscriptionCommand;
import com.closedsource.qualitrack.platform.subscription.domain.model.commands.RecordStripePaymentCommand;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetSubscriptionByStripeCheckoutSessionIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.BillingCycle;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.PaymentStatus;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.PlanCode;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.StripeWebhookResource;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/subscriptions/stripe", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Stripe Webhooks", description = "Stripe subscription webhook endpoints")
@Slf4j
public class StripeWebhookController {

    private final String webhookSecret;
    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;
    private final ExternalStripeService externalStripeService;

    public StripeWebhookController(
            @Value("${stripe.webhook-secret}") String webhookSecret,
            SubscriptionCommandService subscriptionCommandService,
            SubscriptionQueryService subscriptionQueryService,
            ExternalStripeService externalStripeService
    ) {
        this.webhookSecret = webhookSecret;
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.externalStripeService = externalStripeService;
    }

    @PostMapping("/webhook")
    @Operation(summary = "Process Stripe webhook", description = "Processes Stripe checkout and subscription webhook events.")
    public ResponseEntity<StripeWebhookResource> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader
    ) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body(new StripeWebhookResource("invalid-signature"));
        }

        log.info("Stripe webhook received: {}", event.getType());

        if ("checkout.session.completed".equals(event.getType())) {
            processCheckoutSessionCompleted(event);
        }

        return ResponseEntity.ok(new StripeWebhookResource("processed"));
    }

    private void processCheckoutSessionCompleted(Event event) {
        Session session;

        try {
            var stripeObject = event.getDataObjectDeserializer().deserializeUnsafe();

            if (!(stripeObject instanceof Session checkoutSession)) {
                log.warn("Stripe event '{}' was not a checkout session.", event.getType());
                return;
            }

            session = checkoutSession;

        } catch (EventDataObjectDeserializationException e) {
            log.error("Could not deserialize Stripe checkout session event.", e);
            return;
        }

        if (subscriptionQueryService
                .handle(new GetSubscriptionByStripeCheckoutSessionIdQuery(session.getId()))
                .isPresent()) {
            log.info("Checkout session '{}' already processed.", session.getId());
            return;
        }

        var metadata = session.getMetadata();

        var userId = Long.valueOf(metadata.get("userId"));
        var laboratoryId = Long.valueOf(metadata.get("laboratoryId"));
        var planCode = PlanCode.valueOf(metadata.get("planCode"));
        var billingCycle = BillingCycle.valueOf(metadata.get("billingCycle"));

        var stripeSubscriptionDetails = externalStripeService.retrieveSubscriptionDetails(
                session.getSubscription()
        );

        var activateResult = subscriptionCommandService.handle(new ActivateSubscriptionCommand(
                userId,
                laboratoryId,
                planCode,
                billingCycle,
                stripeSubscriptionDetails.stripeCustomerId(),
                stripeSubscriptionDetails.stripeSubscriptionId(),
                session.getId(),
                stripeSubscriptionDetails.currentPeriodStart(),
                stripeSubscriptionDetails.currentPeriodEnd()
        ));

        activateResult.toOptional().ifPresent(subscriptionId -> {
            if (session.getAmountTotal() == null || session.getCurrency() == null) return;

            subscriptionCommandService.handle(new RecordStripePaymentCommand(
                    subscriptionId,
                    session.getPaymentIntent(),
                    session.getId(),
                    BigDecimal.valueOf(session.getAmountTotal()).movePointLeft(2),
                    session.getCurrency().toUpperCase(),
                    PaymentStatus.PAID,
                    OffsetDateTime.now().toString()
            ));

            log.info("Subscription '{}' activated from Stripe checkout session '{}'.", subscriptionId, session.getId());
        });
    }
}