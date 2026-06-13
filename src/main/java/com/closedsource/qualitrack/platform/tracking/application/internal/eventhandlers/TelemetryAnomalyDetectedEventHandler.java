package com.closedsource.qualitrack.platform.tracking.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.tracking.domain.model.events.TelemetryAnomalyDetectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles side effects triggered when a telemetry anomaly is detected.
 */
@Slf4j
@Service
public class TelemetryAnomalyDetectedEventHandler {
    /**
     * Handles the telemetry anomaly detected domain event.
     *
     * @param event the telemetry anomaly detected event
     */
    @EventListener
    public void on(TelemetryAnomalyDetectedEvent event) {
        log.warn(
                "Telemetry anomaly detected. historyPointId={}, equipmentId={}, parameterName={}, recordedValue={}",
                event.historyPointId(),
                event.equipmentId(),
                event.parameterName(),
                event.recordedValue()
        );

        // TODO: In the future, create or escalate deviation alerts through the CA bounded context.
    }
}