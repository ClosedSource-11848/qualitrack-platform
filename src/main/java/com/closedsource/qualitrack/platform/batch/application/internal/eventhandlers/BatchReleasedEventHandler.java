package com.closedsource.qualitrack.platform.batch.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.batch.domain.model.events.BatchReleasedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link BatchReleasedEvent}.
 *
 * <p>Listens for batch release events to trigger reporting, audit trail,
 * or compliance-related side effects.</p>
 */
@Service
@Slf4j
public class BatchReleasedEventHandler {

    /**
     * Default constructor.
     */
    public BatchReleasedEventHandler() {
    }

    /**
     * Handles the {@link BatchReleasedEvent}.
     *
     * @param event the {@link BatchReleasedEvent} published by the batch aggregate
     */
    @EventListener(BatchReleasedEvent.class)
    public void on(BatchReleasedEvent event) {
        log.info("Batch released successfully: ID='{}', BatchNumber='{}', Laboratory ID='{}', Product ID='{}', ReleaseDate='{}'.",
                event.batchId(),
                event.batchNumber(),
                event.laboratoryId(),
                event.productId(),
                event.releaseDate());

        // TODO: En el futuro, inyectar ExternalAuditService o ExternalReportingService
        // para registrar la liberacion del lote en RA.
    }
}