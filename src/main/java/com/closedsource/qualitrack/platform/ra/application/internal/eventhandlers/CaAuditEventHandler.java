package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.ca.interfaces.events.DeviationAlertAcknowledgedIntegrationEvent;
import com.closedsource.qualitrack.platform.ca.interfaces.events.DeviationAlertCreatedIntegrationEvent;
import com.closedsource.qualitrack.platform.ca.interfaces.events.DeviationAlertResolvedIntegrationEvent;
import com.closedsource.qualitrack.platform.ca.interfaces.events.NotificationPreferenceUpdatedIntegrationEvent;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;
import com.closedsource.qualitrack.platform.ra.interfaces.acl.RaContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles Compliance & Alerts integration events and records audit logs in RA.
 */
@Service
@Slf4j
public class CaAuditEventHandler {

    private static final Long SYSTEM_USER_ID = 1L;

    private final RaContextFacade raContextFacade;

    public CaAuditEventHandler(RaContextFacade raContextFacade) {
        this.raContextFacade = raContextFacade;
    }

    @EventListener(DeviationAlertCreatedIntegrationEvent.class)
    public void on(DeviationAlertCreatedIntegrationEvent event) {
        log.info("RA received deviation alert created event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "DEVIATION_ALERT",
                event.alertId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(DeviationAlertAcknowledgedIntegrationEvent.class)
    public void on(DeviationAlertAcknowledgedIntegrationEvent event) {
        log.info("RA received deviation alert acknowledged event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "DEVIATION_ALERT",
                event.alertId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(DeviationAlertResolvedIntegrationEvent.class)
    public void on(DeviationAlertResolvedIntegrationEvent event) {
        log.info("RA received deviation alert resolved event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "DEVIATION_ALERT",
                event.alertId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(NotificationPreferenceUpdatedIntegrationEvent.class)
    public void on(NotificationPreferenceUpdatedIntegrationEvent event) {
        log.info("RA received notification preference updated event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "NOTIFICATION_PREFERENCE",
                event.preferenceId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }
}