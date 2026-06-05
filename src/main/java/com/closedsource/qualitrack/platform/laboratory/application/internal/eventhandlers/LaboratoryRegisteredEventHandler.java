package com.closedsource.qualitrack.platform.laboratory.application.internal.eventhandlers;

import com.closedsource.qualitrack.platform.laboratory.domain.model.events.LaboratoryRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Application-layer event handler for {@link LaboratoryRegisteredEvent}.
 *
 * <p>Listens for laboratory registrations to perform side effects,
 * such as notifying external systems, reporting modules, or updating read models.</p>
 */
@Service
@Slf4j
public class LaboratoryRegisteredEventHandler {

    /**
     * Default constructor.
     */
    public LaboratoryRegisteredEventHandler() {
    }

    /**
     * Handles the {@link LaboratoryRegisteredEvent}.
     *
     * @param event the {@link LaboratoryRegisteredEvent} published by the laboratory aggregate
     */
    @EventListener(LaboratoryRegisteredEvent.class)
    public void on(LaboratoryRegisteredEvent event) {
        log.info("Laboratory registered successfully: ID='{}', Name='{}'. Ready for downstream integration.",
                event.laboratoryId(), event.name());

        // TODO: En el futuro, aquí puedes inyectar un OutboundService (ACL)
        // para avisarle al módulo de Tracking (Equipment/Batch) que hay un nuevo laboratorio.
    }
}