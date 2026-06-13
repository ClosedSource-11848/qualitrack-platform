package com.closedsource.qualitrack.platform.tracking.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.tracking.domain.model.events.MeasurementRecordedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles side effects triggered after a telemetry measurement is recorded.
 */
@Slf4j
@Service
public class MeasurementRecordedEventHandler {
    /**
     * Handles the measurement recorded domain event.
     *
     * @param event the measurement recorded event
     */
    @EventListener
    public void on(MeasurementRecordedEvent event) {
        log.info(
                "Telemetry measurement recorded. measurementId={}, equipmentId={}, parameterName={}, value={}, unit={}",
                event.measurementId(),
                event.equipmentId(),
                event.parameterName(),
                event.value(),
                event.unit()
        );

        // TODO: In the future, publish this measurement to RA for KPI and trend calculation.
    }
}