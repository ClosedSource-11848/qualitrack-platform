package com.closedsource.qualitrack.platform.ra.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.laboratory.interfaces.events.LaboratoryRegisteredIntegrationEvent;
import com.closedsource.qualitrack.platform.laboratory.interfaces.events.ProductCreatedIntegrationEvent;
import com.closedsource.qualitrack.platform.laboratory.interfaces.events.RawMaterialCreatedIntegrationEvent;
import com.closedsource.qualitrack.platform.laboratory.interfaces.events.RawMaterialLowStockIntegrationEvent;
import com.closedsource.qualitrack.platform.laboratory.interfaces.events.StaffDeactivatedIntegrationEvent;
import com.closedsource.qualitrack.platform.laboratory.interfaces.events.StaffRegisteredIntegrationEvent;
import com.closedsource.qualitrack.platform.ra.domain.model.valueobjects.AuditAction;
import com.closedsource.qualitrack.platform.ra.interfaces.acl.RaContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handles Laboratory integration events and records audit logs in RA.
 */
@Service
@Slf4j
public class LaboratoryAuditEventHandler {

    private static final Long SYSTEM_USER_ID = 1L;

    private final RaContextFacade raContextFacade;

    public LaboratoryAuditEventHandler(RaContextFacade raContextFacade) {
        this.raContextFacade = raContextFacade;
    }

    @EventListener(LaboratoryRegisteredIntegrationEvent.class)
    public void on(LaboratoryRegisteredIntegrationEvent event) {
        log.info("RA received laboratory registered event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "LABORATORY",
                event.laboratoryId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(ProductCreatedIntegrationEvent.class)
    public void on(ProductCreatedIntegrationEvent event) {
        log.info("RA received product created event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "PHARMACEUTICAL_PRODUCT",
                event.productId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(RawMaterialCreatedIntegrationEvent.class)
    public void on(RawMaterialCreatedIntegrationEvent event) {
        log.info("RA received raw material created event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "RAW_MATERIAL",
                event.rawMaterialId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(StaffRegisteredIntegrationEvent.class)
    public void on(StaffRegisteredIntegrationEvent event) {
        log.info("RA received staff registered event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.REGISTER,
                "STAFF_MEMBER",
                event.staffMemberId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(StaffDeactivatedIntegrationEvent.class)
    public void on(StaffDeactivatedIntegrationEvent event) {
        log.info("RA received staff deactivated event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "STAFF_MEMBER",
                event.staffMemberId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }

    @EventListener(RawMaterialLowStockIntegrationEvent.class)
    public void on(RawMaterialLowStockIntegrationEvent event) {
        log.info("RA received raw material low stock event: {}", event);

        raContextFacade.recordAuditLog(
                AuditAction.UPDATE,
                "RAW_MATERIAL",
                event.rawMaterialId(),
                SYSTEM_USER_ID,
                event.toString()
        );
    }
}