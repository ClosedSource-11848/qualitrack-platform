package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;
import com.closedsource.qualitrack.platform.ra.interfaces.acl.RaContextFacade;
import com.closedsource.qualitrack.platform.tracking.interfaces.events.EquipmentTelemetryStatusUpdatedIntegrationEvent;
import com.closedsource.qualitrack.platform.tracking.interfaces.events.MeasurementRecordedIntegrationEvent;
import com.closedsource.qualitrack.platform.tracking.interfaces.events.TelemetryAnomalyDetectedIntegrationEvent;
import com.closedsource.qualitrack.platform.tracking.interfaces.events.TelemetryHistoryPointRecordedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles Tracking integration events and records audit logs in RA.
 */
@Service
@Slf4j
public class TrackingAuditEventHandler {

    private static final Long SYSTEM_USER_ID = 1L;

    private final RaContextFacade raContextFacade;

    /**
     * Creates the handler with the Reporting & Audit ACL facade.
     *
     * @param raContextFacade RA facade used to record audit logs
     */
    public TrackingAuditEventHandler(RaContextFacade raContextFacade) {
        this.raContextFacade = raContextFacade;
    }

    /**
     * Handles telemetry measurement recorded integration events.
     *
     * @param event Tracking measurement integration event
     */
    @EventListener(MeasurementRecordedIntegrationEvent.class)
    public void on(MeasurementRecordedIntegrationEvent event) {
        log.info("RA received measurement recorded event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "TELEMETRY_MEASUREMENT",
                event.measurementId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    /**
     * Handles telemetry history point recorded integration events.
     *
     * @param event Tracking telemetry history point integration event
     */
    @EventListener(TelemetryHistoryPointRecordedIntegrationEvent.class)
    public void on(TelemetryHistoryPointRecordedIntegrationEvent event) {
        log.info("RA received telemetry history point recorded event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "TELEMETRY_HISTORY_POINT",
                event.historyPointId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    /**
     * Handles equipment telemetry status updated integration events.
     *
     * @param event Tracking equipment telemetry status integration event
     */
    @EventListener(EquipmentTelemetryStatusUpdatedIntegrationEvent.class)
    public void on(EquipmentTelemetryStatusUpdatedIntegrationEvent event) {
        log.info("RA received equipment telemetry status updated event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "EQUIPMENT_TELEMETRY_STATUS",
                event.statusId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    /**
     * Handles telemetry anomaly detected integration events.
     *
     * @param event Tracking telemetry anomaly integration event
     */
    @EventListener(TelemetryAnomalyDetectedIntegrationEvent.class)
    public void on(TelemetryAnomalyDetectedIntegrationEvent event) {
        log.warn("RA received telemetry anomaly detected event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "TELEMETRY_ANOMALY",
                event.historyPointId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }
}