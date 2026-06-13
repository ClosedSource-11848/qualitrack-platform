package com.closedsource.qualitrack.platform.ca.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ca.application.commandservices.CaCommandService;
import com.closedsource.qualitrack.platform.ca.domain.model.commands.CreateDeviationAlertCommand;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.AlertSeverity;
import com.closedsource.qualitrack.platform.tracking.interfaces.events.TelemetryAnomalyDetectedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Handles telemetry anomaly integration events by creating deviation alerts.
 */
@Service
@Slf4j
public class TelemetryAnomalyDetectedComplianceEventHandler {

    private final CaCommandService caCommandService;

    public TelemetryAnomalyDetectedComplianceEventHandler(CaCommandService caCommandService) {
        this.caCommandService = caCommandService;
    }

    @EventListener(TelemetryAnomalyDetectedIntegrationEvent.class)
    public void on(TelemetryAnomalyDetectedIntegrationEvent event) {
        log.warn(
                "CA received telemetry anomaly event: History Point ID='{}', Equipment ID='{}', Parameter='{}', Value='{}', Timestamp='{}'.",
                event.historyPointId(),
                event.equipmentId(),
                event.parameterName(),
                event.recordedValue(),
                event.timestamp()
        );

        caCommandService.handle(new CreateDeviationAlertCommand(
                event.equipmentId(),
                null,
                event.parameterName(),
                event.recordedValue(),
                null,
                "telemetry",
                Instant.now().toString(),
                AlertSeverity.CRITICAL
        ));
    }
}