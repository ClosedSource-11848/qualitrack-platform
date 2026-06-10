package com.closedsource.qualitrack.platform.ca.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.batch.domain.model.events.BatchRejectedEvent;
import com.closedsource.qualitrack.platform.ca.domain.model.entities.ComplianceEvent;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.ComplianceEventType;
import com.closedsource.qualitrack.platform.ca.domain.repositories.ComplianceEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Handles batch rejection events by recording compliance audit events.
 */
@Service
@Slf4j
public class BatchRejectedComplianceEventHandler {

    private final ComplianceEventRepository complianceEventRepository;

    public BatchRejectedComplianceEventHandler(ComplianceEventRepository complianceEventRepository) {
        this.complianceEventRepository = complianceEventRepository;
    }

    @EventListener(BatchRejectedEvent.class)
    public void on(BatchRejectedEvent event) {
        log.warn("CA received batch rejected event for batch ID '{}'.", event.batchId());

        complianceEventRepository.save(new ComplianceEvent(
                event.batchId(),
                ComplianceEventType.BATCH_REJECTED,
                "Batch '%s' was rejected. Reason: %s.".formatted(event.batchNumber(), event.reason()),
                Instant.now().toString(),
                null
        ));
    }
}