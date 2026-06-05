package com.closedsource.qualitrack.platform.laboratory.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.laboratory.domain.model.events.RawMaterialLowStockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link RawMaterialLowStockEvent}.
 *
 * <p>Listens for low stock alerts to trigger external notifications.
 * This is critical for communicating with the Compliance & Alerts (CA) bounded context
 * without creating a tight coupling between the modules.</p>
 */
@Service
@Slf4j
public class RawMaterialLowStockEventHandler {

    /**
     * Default constructor.
     */
    public RawMaterialLowStockEventHandler() {
    }

    /**
     * Handles the {@link RawMaterialLowStockEvent}.
     *
     * <p>Logs a critical warning when a material falls below its safety threshold.</p>
     *
     * @param event the {@link RawMaterialLowStockEvent} published by the raw material aggregate
     */
    @EventListener(RawMaterialLowStockEvent.class)
    public void on(RawMaterialLowStockEvent event) {
        log.warn("LOW STOCK ALERT CAUGHT: Material '{}' (ID: {}) in Laboratory '{}' has dropped to {} units. Minimum safety threshold is {}.",
                event.materialName(),
                event.rawMaterialId(),
                event.laboratoryId(),
                event.currentStock(),
                event.minimumThreshold());

        // TODO: En el futuro, inyectar ExternalComplianceService (del paquete acl)
        // externalComplianceService.notifyLowStockDeviation(event);
    }
}