package com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateLaboratoryCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.UpdateLaboratoryCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.entities.LaboratoryAddress;
import com.closedsource.qualitrack.platform.laboratory.domain.model.events.LaboratoryRegisteredEvent;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryName;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryStatus;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.Regulation;
import com.closedsource.qualitrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * Laboratory aggregate root.
 *
 * <p>Represents a pharmaceutical laboratory within the platform.
 * Governs the state of the laboratory, its profile, and regulatory compliance status.</p>
 */
public class Laboratory extends AbstractDomainAggregateRoot<Laboratory> {

    /**
     * The unique business identifier for the laboratory.
     */
    @Getter
    @Setter
    private String id;

    @Getter
    private LaboratoryName name;

    @Getter
    private Regulation regulation;

    @Getter
    private LaboratoryStatus status;

    @Getter
    private LaboratoryAddress address;

    /**
     * Default constructor for Laboratory.
     * Required for reconstruction from persistence.
     */
    public Laboratory() {
        // Required for reconstruction
    }

    /**
     * Constructor for reconstruction from the persistence layer (Assembler).
     * * @param id The laboratory ID.
     * @param name The laboratory name.
     * @param regulation The regulation code.
     * @param status The current status.
     * @param address The laboratory address.
     */
    public Laboratory(String id, LaboratoryName name, Regulation regulation, LaboratoryStatus status, LaboratoryAddress address) {
        this.id = id;
        this.name = name;
        this.regulation = regulation;
        this.status = status;
        this.address = address;
    }

    /**
     * Constructor for Laboratory with a CreateLaboratoryCommand.
     * Initializes a new laboratory with ACTIVE status.
     * * @param command The CreateLaboratoryCommand.
     */
    public Laboratory(CreateLaboratoryCommand command) {
        this.id = UUID.randomUUID().toString();
        this.name = new LaboratoryName(command.name());
        this.regulation = new Regulation(command.regulationCode());
        this.status = LaboratoryStatus.ACTIVE;
        this.address = new LaboratoryAddress(command.street(), command.city(), command.zipCode());
    }

    /**
     * Updates the laboratory profile information.
     * * @param command The UpdateLaboratoryCommand.
     */
    public void updateProfile(UpdateLaboratoryCommand command) {
        this.name = new LaboratoryName(command.name());
        this.regulation = new Regulation(command.regulationCode());
    }

    /**
     * Suspends the laboratory operations.
     */
    public void suspendOperations() {
        if (this.status == LaboratoryStatus.SUSPENDED) {
            throw new IllegalStateException("Laboratory is already suspended");
        }
        this.status = LaboratoryStatus.SUSPENDED;
    }

    /**
     * Signals that this laboratory has just been registered.
     * Registers a {@link LaboratoryRegisteredEvent} for other bounded contexts.
     */
    public void onRegistered() {
        registerDomainEvent(LaboratoryRegisteredEvent.from(this));
    }
}