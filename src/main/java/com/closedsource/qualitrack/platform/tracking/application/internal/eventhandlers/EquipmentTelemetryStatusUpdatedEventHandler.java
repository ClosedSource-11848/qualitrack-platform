package com.closedsource.qualitrack.platform.tracking.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.tracking.domain.model.events.EquipmentTelemetryStatusUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles side effects triggered after equipment telemetry status is updated.
 */
@Slf4j
@Service
public class EquipmentTelemetryStatusUpdatedEventHandler {
    /**
     * Handles the equipment telemetry status updated domain event.
     *
     * @param event the equipment telemetry status updated event
     */
    @EventListener
    public void on(EquipmentTelemetryStatusUpdatedEvent event) {
        log.info(
                "Equipment telemetry status updated. statusId={}, equipmentId={}, isOnline={}, status={}",
                event.statusId(),
                event.equipmentId(),
                event.isOnline(),
                event.currentStatus()
        );

        // TODO: In the future, synchronize equipment operational indicators with the Equipment BC.
    }
}