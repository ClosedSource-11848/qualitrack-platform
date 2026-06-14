package com.closedsource.qualitrack.platform.subscription.interfaces.rest;

import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.closedsource.qualitrack.platform.subscription.application.commandservices.SubscriptionCommandService;
import com.closedsource.qualitrack.platform.subscription.application.queryservices.SubscriptionQueryService;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetActiveSubscriptionByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetBillingSummaryByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetPaymentsBySubscriptionIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetSubscriptionPlansQuery;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.CancelSubscriptionResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.CheckoutSessionResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.CreateCheckoutSessionResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.SubscriptionPaymentResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.SubscriptionPlanResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.SubscriptionResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.transform.CancelSubscriptionCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.transform.CreateCheckoutSessionCommandFromResourceAssembler;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.transform.SubscriptionPaymentResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.transform.SubscriptionPlanResourceFromEntityAssembler;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes subscription and billing endpoints.
 */
@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Subscription and billing endpoints")
public class SubscriptionController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionController(
            SubscriptionCommandService subscriptionCommandService,
            SubscriptionQueryService subscriptionQueryService
    ) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @GetMapping("/plans")
    @Operation(summary = "Get subscription plans", description = "Retrieves all active subscription plans.")
    public ResponseEntity<List<SubscriptionPlanResource>> getPlans() {
        var plans = subscriptionQueryService.handle(new GetSubscriptionPlansQuery());

        var resources = plans.stream()
                .map(SubscriptionPlanResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PostMapping("/checkout-sessions")
    @Operation(summary = "Create Stripe checkout session", description = "Creates a Stripe Checkout Session for a subscription plan.")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CreateCheckoutSessionResource resource) {
        var command = CreateCheckoutSessionCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                CheckoutSessionResource::new,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/laboratories/{laboratoryId}/active")
    @Operation(summary = "Get active subscription by laboratory", description = "Retrieves the active subscription for a laboratory.")
    public ResponseEntity<SubscriptionResource> getActiveSubscriptionByLaboratoryId(
            @PathVariable Long laboratoryId
    ) {
        var subscription = subscriptionQueryService.handle(
                new GetActiveSubscriptionByLaboratoryIdQuery(laboratoryId)
        );

        if (subscription.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(
                SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription.get())
        );
    }

    @GetMapping("/laboratories/{laboratoryId}/billing-summary")
    @Operation(summary = "Get billing summary by laboratory", description = "Retrieves subscription history for a laboratory.")
    public ResponseEntity<List<SubscriptionResource>> getBillingSummaryByLaboratoryId(
            @PathVariable Long laboratoryId
    ) {
        var subscriptions = subscriptionQueryService.handle(
                new GetBillingSummaryByLaboratoryIdQuery(laboratoryId)
        );

        var resources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{subscriptionId}/payments")
    @Operation(summary = "Get subscription payments", description = "Retrieves payment history for a subscription.")
    public ResponseEntity<List<SubscriptionPaymentResource>> getPaymentsBySubscriptionId(
            @PathVariable Long subscriptionId
    ) {
        var payments = subscriptionQueryService.handle(
                new GetPaymentsBySubscriptionIdQuery(subscriptionId)
        );

        var resources = payments.stream()
                .map(SubscriptionPaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{subscriptionId}/cancel")
    @Operation(summary = "Cancel subscription", description = "Cancels an active Stripe subscription and marks it as cancelled locally.")
    public ResponseEntity<?> cancelSubscription(
            @PathVariable Long subscriptionId,
            @RequestBody CancelSubscriptionResource resource
    ) {
        var command = CancelSubscriptionCommandFromResourceAssembler.toCommandFromResource(
                subscriptionId,
                resource
        );

        var result = subscriptionCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                id -> id,
                HttpStatus.OK
        );
    }
}