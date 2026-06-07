package com.closedsource.qualitrack.platform.equipment.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.equipment.domain.model.events.CalibrationExpiredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link CalibrationExpiredEvent}.
 *
 * <p>Listens for equipment calibration expiration alerts to trigger external notifications.
 * This is critical for communicating with the Compliance & Alerts (CA) bounded context
 * to halt equipment usage and ensure DIGEMID regulatory compliance.</p>
 */
@Service
@Slf4j
public class CalibrationExpiryEventHandler {

    /**
     * Default constructor.
     */
    public CalibrationExpiryEventHandler() {
    }

    /**
     * Handles the {@link CalibrationExpiredEvent}.
     *
     * <p>Logs a critical warning when an equipment's calibration validity period expires.</p>
     *
     * @param event the {@link CalibrationExpiredEvent} published by the equipment aggregate
     */
    @EventListener(CalibrationExpiredEvent.class)
    public void on(CalibrationExpiredEvent event) {
        log.warn("CALIBRATION EXPIRED ALERT: Equipment '{}' (SN: {}) in Laboratory ID {} has an expired calibration. Immediate action required for regulatory compliance.",
                event.equipmentName(),
                event.serialNumber(),
                event.laboratoryId());

        // TODO: En el futuro, inyectar ExternalComplianceService (del paquete acl)
        // para disparar notificaciones push a los gerentes de calidad o bloquear
        // automáticamente el uso del equipo en otros módulos.
        // externalComplianceService.notifyCalibrationExpiration(event);
    }
}