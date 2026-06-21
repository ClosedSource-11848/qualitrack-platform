package com.closedsource.qualitrack.platform.subscription.interfaces.rest;

import com.closedsource.qualitrack.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.closedsource.qualitrack.platform.subscription.application.commandservices.SubscriptionCommandService;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.CheckoutSessionResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.resources.CreateCheckoutSessionResource;
import com.closedsource.qualitrack.platform.subscription.interfaces.rest.transform.CreateCheckoutSessionCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller that exposes subscription checkout session resources.
 */
@RestController
@RequestMapping(value = "/api/v1/subscription-checkout-sessions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Subscription Checkout Sessions", description = "Subscription checkout session endpoints")
public class SubscriptionCheckoutSessionController {

    private final SubscriptionCommandService subscriptionCommandService;

    public SubscriptionCheckoutSessionController(SubscriptionCommandService subscriptionCommandService) {
        this.subscriptionCommandService = subscriptionCommandService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create subscription checkout session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CreateCheckoutSessionResource resource) {
        var command = CreateCheckoutSessionCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionCommandService.handle(command);

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                CheckoutSessionResource::new,
                HttpStatus.CREATED
        );
    }
}