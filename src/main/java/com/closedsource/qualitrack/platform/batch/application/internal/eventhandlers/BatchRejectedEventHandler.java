package com.closedsource.qualitrack.platform.batch.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.batch.domain.model.events.BatchRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link BatchRejectedEvent}.
 *
 * <p>Listens for batch rejection events to trigger audit, compliance alerts,
 * or quality-control reporting.</p>
 */
@Service
@Slf4j
public class BatchRejectedEventHandler {

    /**
     * Default constructor.
     */
    public BatchRejectedEventHandler() {
    }

    /**
     * Handles the {@link BatchRejectedEvent}.
     *
     * @param event the {@link BatchRejectedEvent} published by the batch aggregate
     */
    @EventListener(BatchRejectedEvent.class)
    public void on(BatchRejectedEvent event) {
        log.warn("BATCH REJECTED ALERT: Batch '{}' (ID: {}) in Laboratory ID {} was rejected on '{}'. Reason: {}",
                event.batchNumber(),
                event.batchId(),
                event.laboratoryId(),
                event.rejectionDate(),
                event.reason());

        // TODO: En el futuro, inyectar ExternalComplianceService
        // para disparar alertas de desviacion o registrar el evento en CA.
    }
}