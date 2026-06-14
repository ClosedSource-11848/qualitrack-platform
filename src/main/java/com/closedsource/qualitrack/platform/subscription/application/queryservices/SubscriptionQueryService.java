package com.closedsource.qualitrack.platform.subscription.application.queryservices;

import com.closedsource.qualitrack.platform.subscription.domain.model.aggregates.Subscription;
import com.closedsource.qualitrack.platform.subscription.domain.model.entities.SubscriptionPayment;
import com.closedsource.qualitrack.platform.subscription.domain.model.entities.SubscriptionPlan;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetActiveSubscriptionByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetBillingSummaryByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetPaymentsBySubscriptionIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetSubscriptionByStripeCheckoutSessionIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetSubscriptionPlansQuery;

import java.util.List;
import java.util.Optional;

/**
 * Application query service contract for subscription read operations.
 */
public interface SubscriptionQueryService {

    List<SubscriptionPlan> handle(GetSubscriptionPlansQuery query);

    Optional<Subscription> handle(GetActiveSubscriptionByLaboratoryIdQuery query);

    Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query);

    List<Subscription> handle(GetBillingSummaryByLaboratoryIdQuery query);

    List<SubscriptionPayment> handle(GetPaymentsBySubscriptionIdQuery query);

    Optional<Subscription> handle(GetSubscriptionByStripeCheckoutSessionIdQuery query);
}