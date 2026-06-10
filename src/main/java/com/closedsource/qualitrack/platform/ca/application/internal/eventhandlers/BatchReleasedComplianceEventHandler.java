package com.closedsource.qualitrack.platform.ca.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.batch.domain.model.events.BatchReleasedEvent;
import com.closedsource.qualitrack.platform.ca.domain.model.entities.ComplianceEvent;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.ComplianceEventType;
import com.closedsource.qualitrack.platform.ca.domain.repositories.ComplianceEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Handles batch release events by recording compliance audit events.
 */
@Service
@Slf4j
public class BatchReleasedComplianceEventHandler {

    private final ComplianceEventRepository complianceEventRepository;

    public BatchReleasedComplianceEventHandler(ComplianceEventRepository complianceEventRepository) {
        this.complianceEventRepository = complianceEventRepository;
    }

    @EventListener(BatchReleasedEvent.class)
    public void on(BatchReleasedEvent event) {
        log.info("CA received batch released event for batch ID '{}'.", event.batchId());

        complianceEventRepository.save(new ComplianceEvent(
                event.batchId(),
                ComplianceEventType.BATCH_RELEASED,
                "Batch '%s' was released on %s.".formatted(event.batchNumber(), event.releaseDate()),
                Instant.now().toString(),
                null
        ));
    }
}