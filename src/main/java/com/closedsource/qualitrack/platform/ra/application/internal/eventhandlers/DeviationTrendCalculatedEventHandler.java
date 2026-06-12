package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ra.domain.model.events.DeviationTrendCalculatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link DeviationTrendCalculatedEvent}.
 *
 * <p>Listens for deviation trend calculation events to support downstream
 * analytics, monitoring, or compliance notifications.</p>
 */
@Service
@Slf4j
public class DeviationTrendCalculatedEventHandler {

    /**
     * Default constructor.
     */
    public DeviationTrendCalculatedEventHandler() {
    }

    /**
     * Handles the {@link DeviationTrendCalculatedEvent}.
     *
     * @param event the {@link DeviationTrendCalculatedEvent} published by the RA bounded context
     */
    @EventListener(DeviationTrendCalculatedEvent.class)
    public void on(DeviationTrendCalculatedEvent event) {
        log.info(
                "Deviation trend calculated: Trend ID='{}', Equipment ID='{}', Parameter='{}', Direction='{}', CalculatedAt='{}'.",
                event.trendId(),
                event.equipmentId(),
                event.parameterName(),
                event.trendDirection(),
                event.calculatedAt()
        );

        // TODO: In the future, notify CA when a worsening trend requires compliance attention.
    }
}