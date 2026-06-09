package com.closedsource.qualitrack.platform.batch.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.batch.domain.model.events.BatchCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link BatchCreatedEvent}.
 *
 * <p>Listens for batch creation events to perform side effects such as notifying
 * reporting, compliance, or audit modules.</p>
 */
@Service
@Slf4j
public class BatchCreatedEventHandler {

    /**
     * Default constructor.
     */
    public BatchCreatedEventHandler() {
    }

    /**
     * Handles the {@link BatchCreatedEvent}.
     *
     * @param event the {@link BatchCreatedEvent} published by the batch aggregate
     */
    @EventListener(BatchCreatedEvent.class)
    public void on(BatchCreatedEvent event) {
        log.info("Batch created successfully: ID='{}', BatchNumber='{}', Laboratory ID='{}', Product ID='{}'. Ready for downstream integration.",
                event.batchId(),
                event.batchNumber(),
                event.laboratoryId(),
                event.productId());

        // TODO: En el futuro, inyectar un OutboundService (ACL)
        // para notificar al modulo de Reporting & Audit (RA) o Compliance & Alerts (CA).
    }
}