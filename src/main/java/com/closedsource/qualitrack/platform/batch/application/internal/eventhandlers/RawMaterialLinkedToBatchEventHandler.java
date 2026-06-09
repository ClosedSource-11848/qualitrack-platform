package com.closedsource.qualitrack.platform.batch.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.batch.domain.model.events.RawMaterialLinkedToBatchEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link RawMaterialLinkedToBatchEvent}.
 *
 * <p>Listens for raw material usage linkage events to support traceability,
 * inventory synchronization, and reporting.</p>
 */
@Service
@Slf4j
public class RawMaterialLinkedToBatchEventHandler {

    /**
     * Default constructor.
     */
    public RawMaterialLinkedToBatchEventHandler() {
    }

    /**
     * Handles the {@link RawMaterialLinkedToBatchEvent}.
     *
     * @param event the {@link RawMaterialLinkedToBatchEvent} published after linking a raw material to a batch
     */
    @EventListener(RawMaterialLinkedToBatchEvent.class)
    public void on(RawMaterialLinkedToBatchEvent event) {
        log.info("Raw material linked to batch: Usage ID='{}', Batch ID='{}', Material='{}' (ID: {}), Quantity='{} {}'.",
                event.usageId(),
                event.batchId(),
                event.rawMaterialName(),
                event.rawMaterialId(),
                event.quantityUsed(),
                event.unit());

        // TODO: En el futuro, inyectar un OutboundService (ACL)
        // para sincronizar consumo de inventario o alimentar reportes de trazabilidad.
    }
}