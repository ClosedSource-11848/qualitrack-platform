package com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.RegisterStaffCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.events.StaffRegisteredEvent;
import com.closedsource.qualitrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * StaffMember aggregate root.
 *
 * <p>Represents an employee working within a specific laboratory.</p>
 */
@Getter
public class StaffMember extends AbstractDomainAggregateRoot<StaffMember> {

    @Setter
    private String id;
    private String laboratoryId;
    private String firstName;
    private String lastName;
    private String role;
    private boolean active;

    public StaffMember() {
        // Required for reconstruction
    }

    /**
     * Constructor for reconstruction from the persistence layer (Assembler).
     */
    public StaffMember(String id, String laboratoryId, String firstName, String lastName, String role, boolean active) {
        this.id = id;
        this.laboratoryId = laboratoryId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.active = active;
    }

    /**
     * Constructor for StaffMember with a RegisterStaffCommand.
     * * @param command The RegisterStaffCommand.
     */
    public StaffMember(RegisterStaffCommand command) {
        this.id = UUID.randomUUID().toString();
        this.laboratoryId = Objects.requireNonNull(command.laboratoryId(), "Laboratory ID is required");
        this.firstName = Objects.requireNonNull(command.firstName(), "First name is required");
        this.lastName = Objects.requireNonNull(command.lastName(), "Last name is required");
        this.role = Objects.requireNonNull(command.role(), "Role is required");
        this.active = true;

        // Register the event immediately upon creation
        this.registerDomainEvent(new StaffRegisteredEvent(this.id, this.laboratoryId, getFullName(), this.role));
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    /**
     * Deactivates the staff member.
     */
    public void deactivate() {
        this.active = false;
    }
}