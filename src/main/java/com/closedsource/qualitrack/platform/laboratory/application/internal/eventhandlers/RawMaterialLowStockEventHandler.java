package com.closedsource.qualitrack.platform.laboratory.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.laboratory.application.internal.outboundservices.acl.ExternalComplianceService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.events.RawMaterialLowStockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link RawMaterialLowStockEvent}.
 *
 * <p>Listens for low stock alerts and delegates integration with the Compliance & Alerts
 * bounded context through an outbound ACL service.</p>
 */
@Service
@Slf4j
public class RawMaterialLowStockEventHandler {

    private final ExternalComplianceService externalComplianceService;

    public RawMaterialLowStockEventHandler(ExternalComplianceService externalComplianceService) {
        this.externalComplianceService = externalComplianceService;
    }

    /**
     * Handles the {@link RawMaterialLowStockEvent}.
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

        externalComplianceService.notifyLowStockDeviation(event);
    }
}