package com.closedsource.qualitrack.platform.subscription.application.internal.queryservices;

import com.closedsource.qualitrack.platform.subscription.application.queryservices.SubscriptionQueryService;
import com.closedsource.qualitrack.platform.subscription.domain.model.aggregates.Subscription;
import com.closedsource.qualitrack.platform.subscription.domain.model.entities.SubscriptionPayment;
import com.closedsource.qualitrack.platform.subscription.domain.model.entities.SubscriptionPlan;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetActiveSubscriptionByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetActiveSubscriptionByUserIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetBillingSummaryByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetPaymentsBySubscriptionIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetSubscriptionByStripeCheckoutSessionIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetSubscriptionPlansQuery;
import com.closedsource.qualitrack.platform.subscription.domain.repositories.SubscriptionPaymentRepository;
import com.closedsource.qualitrack.platform.subscription.domain.repositories.SubscriptionPlanRepository;
import com.closedsource.qualitrack.platform.subscription.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementation for subscription queries.
 */
@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;

    public SubscriptionQueryServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository,
            SubscriptionRepository subscriptionRepository,
            SubscriptionPaymentRepository subscriptionPaymentRepository
    ) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPaymentRepository = subscriptionPaymentRepository;
    }

    @Override
    public List<SubscriptionPlan> handle(GetSubscriptionPlansQuery query) {
        return subscriptionPlanRepository.findAllActive();
    }

    @Override
    public Optional<Subscription> handle(GetActiveSubscriptionByLaboratoryIdQuery query) {
        return subscriptionRepository.findActiveByLaboratoryId(query.laboratoryId());
    }

    @Override
    public Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query) {
        return subscriptionRepository.findActiveByUserId(query.userId());
    }

    @Override
    public List<Subscription> handle(GetBillingSummaryByLaboratoryIdQuery query) {
        return subscriptionRepository.findAllByLaboratoryId(query.laboratoryId());
    }

    @Override
    public List<SubscriptionPayment> handle(GetPaymentsBySubscriptionIdQuery query) {
        return subscriptionPaymentRepository.findAllBySubscriptionId(query.subscriptionId());
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByStripeCheckoutSessionIdQuery query) {
        return subscriptionRepository.findByStripeCheckoutSessionId(query.stripeCheckoutSessionId());
    }
}