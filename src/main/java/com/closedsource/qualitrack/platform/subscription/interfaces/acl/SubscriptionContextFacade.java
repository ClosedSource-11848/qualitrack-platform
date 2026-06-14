package com.closedsource.qualitrack.platform.subscription.interfaces.acl;

import com.closedsource.qualitrack.platform.subscription.domain.model.queries.GetActiveSubscriptionByLaboratoryIdQuery;
import com.closedsource.qualitrack.platform.subscription.domain.model.valueobjects.PlanCode;
import com.closedsource.qualitrack.platform.subscription.application.queryservices.SubscriptionQueryService;
import org.springframework.stereotype.Service;

/**
 * ACL facade exposed by the Subscription bounded context.
 *
 * <p>Provides a stable interface for other bounded contexts to verify
 * subscription capabilities without depending on subscription internals.</p>
 */
@Service
public class SubscriptionContextFacade {

    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionContextFacade(SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionQueryService = subscriptionQueryService;
    }

    /**
     * Checks whether a laboratory currently has an active subscription.
     *
     * @param laboratoryId laboratory identifier
     * @return true if the laboratory has an active subscription, otherwise false
     */
    public boolean hasActiveSubscription(Long laboratoryId) {
        return subscriptionQueryService
                .handle(new GetActiveSubscriptionByLaboratoryIdQuery(laboratoryId))
                .isPresent();
    }

    /**
     * Checks whether a laboratory has an active subscription with the given plan.
     *
     * @param laboratoryId laboratory identifier
     * @param planCode expected plan code
     * @return true if the active subscription matches the requested plan
     */
    public boolean hasActivePlan(Long laboratoryId, PlanCode planCode) {
        return subscriptionQueryService
                .handle(new GetActiveSubscriptionByLaboratoryIdQuery(laboratoryId))
                .map(subscription -> subscription.getPlanCode() == planCode)
                .orElse(false);
    }

    /**
     * Gets the current active plan code for a laboratory.
     *
     * @param laboratoryId laboratory identifier
     * @return active plan name, or null if no active subscription exists
     */
    public String getActivePlanCode(Long laboratoryId) {
        return subscriptionQueryService
                .handle(new GetActiveSubscriptionByLaboratoryIdQuery(laboratoryId))
                .map(subscription -> subscription.getPlanCode().name())
                .orElse(null);
    }
}