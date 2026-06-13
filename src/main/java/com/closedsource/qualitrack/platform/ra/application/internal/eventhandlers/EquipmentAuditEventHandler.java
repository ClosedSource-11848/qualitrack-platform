package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.equipment.interfaces.events.BpmParameterConfiguredIntegrationEvent;
import com.closedsource.qualitrack.platform.equipment.interfaces.events.CalibrationExpiredIntegrationEvent;
import com.closedsource.qualitrack.platform.equipment.interfaces.events.EquipmentRegisteredIntegrationEvent;
import com.closedsource.qualitrack.platform.equipment.interfaces.events.MaintenanceRegisteredIntegrationEvent;
import com.closedsource.qualitrack.platform.equipment.interfaces.events.SensorLinkedIntegrationEvent;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;
import com.closedsource.qualitrack.platform.ra.interfaces.acl.RaContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles Equipment integration events and records audit logs in RA.
 */
@Service
@Slf4j
public class EquipmentAuditEventHandler {

    private static final Long SYSTEM_USER_ID = 1L;

    private final RaContextFacade raContextFacade;

    public EquipmentAuditEventHandler(RaContextFacade raContextFacade) {
        this.raContextFacade = raContextFacade;
    }

    @EventListener(EquipmentRegisteredIntegrationEvent.class)
    public void on(EquipmentRegisteredIntegrationEvent event) {
        log.info("RA received equipment registered event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "EQUIPMENT",
                event.equipmentId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(BpmParameterConfiguredIntegrationEvent.class)
    public void on(BpmParameterConfiguredIntegrationEvent event) {
        log.info("RA received BPM parameter configured event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "BPM_PARAMETER_CONFIG",
                event.configId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(MaintenanceRegisteredIntegrationEvent.class)
    public void on(MaintenanceRegisteredIntegrationEvent event) {
        log.info("RA received maintenance registered event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "MAINTENANCE_RECORD",
                event.maintenanceRecordId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(CalibrationExpiredIntegrationEvent.class)
    public void on(CalibrationExpiredIntegrationEvent event) {
        log.info("RA received calibration expired event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "EQUIPMENT",
                event.equipmentId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(SensorLinkedIntegrationEvent.class)
    public void on(SensorLinkedIntegrationEvent event) {
        log.info("RA received sensor linked event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "EQUIPMENT",
                event.equipmentId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }
}