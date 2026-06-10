package com.closedsource.qualitrack.platform.ca.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ca.application.commandservices.CaCommandService;
import com.closedsource.qualitrack.platform.ca.domain.model.commands.CreateDeviationAlertCommand;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.AlertSeverity;
import com.closedsource.qualitrack.platform.equipment.domain.model.events.CalibrationExpiredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Handles equipment calibration expiration events by creating critical deviation alerts.
 */
@Service
@Slf4j
public class CalibrationExpiredEventHandler {

    private final CaCommandService caCommandService;

    public CalibrationExpiredEventHandler(CaCommandService caCommandService) {
        this.caCommandService = caCommandService;
    }

    @EventListener(CalibrationExpiredEvent.class)
    public void on(CalibrationExpiredEvent event) {
        log.warn("CA received calibration expired event for equipment ID '{}'.", event.equipmentId());

        caCommandService.handle(new CreateDeviationAlertCommand(
                event.equipmentId(),
                null,
                "CALIBRATION_STATUS",
                1.0,
                0.0,
                "status",
                Instant.now().toString(),
                AlertSeverity.CRITICAL
        ));
    }
}