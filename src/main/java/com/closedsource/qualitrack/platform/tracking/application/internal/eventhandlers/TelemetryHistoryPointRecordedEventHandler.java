package com.closedsource.qualitrack.platform.tracking.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.tracking.domain.model.events.TelemetryHistoryPointRecordedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles side effects triggered after a telemetry history point is recorded.
 */
@Slf4j
@Service
public class TelemetryHistoryPointRecordedEventHandler {
    /**
     * Handles the telemetry history point recorded domain event.
     *
     * @param event the telemetry history point recorded event
     */
    @EventListener
    public void on(TelemetryHistoryPointRecordedEvent event) {
        log.info(
                "Telemetry history point recorded. historyPointId={}, equipmentId={}, parameterName={}, isAnomaly={}",
                event.historyPointId(),
                event.equipmentId(),
                event.parameterName(),
                event.isAnomaly()
        );

        // TODO: In the future, feed RA deviation trends with this telemetry point.
    }
}