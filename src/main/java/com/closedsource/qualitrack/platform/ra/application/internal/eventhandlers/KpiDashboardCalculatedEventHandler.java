package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ra.domain.model.events.KpiDashboardCalculatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link KpiDashboardCalculatedEvent}.
 *
 * <p>Listens for KPI dashboard calculation events to support downstream
 * reporting, analytics, or notification side effects.</p>
 */
@Service
@Slf4j
public class KpiDashboardCalculatedEventHandler {

    /**
     * Default constructor.
     */
    public KpiDashboardCalculatedEventHandler() {
    }

    /**
     * Handles the {@link KpiDashboardCalculatedEvent}.
     *
     * @param event the {@link KpiDashboardCalculatedEvent} published by the RA bounded context
     */
    @EventListener(KpiDashboardCalculatedEvent.class)
    public void on(KpiDashboardCalculatedEvent event) {
        log.info(
                "KPI dashboard calculated: Dashboard ID='{}', Laboratory ID='{}', Overall Health='{}', Metric Count='{}', CalculatedAt='{}'.",
                event.dashboardId(),
                event.laboratoryId(),
                event.overallHealthScore(),
                event.metricCount(),
                event.calculatedAt()
        );

        // TODO: In the future, trigger alerting when dashboard health is critical.
    }
}