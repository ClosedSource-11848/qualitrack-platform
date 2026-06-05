package com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateProductCommand;
import com.closedsource.qualitrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * PharmaceuticalProduct aggregate root.
 *
 * <p>Represents a product manufactured by a laboratory.</p>
 */
@Getter
public class PharmaceuticalProduct extends AbstractDomainAggregateRoot<PharmaceuticalProduct> {

    @Setter
    private String id;
    private String laboratoryId;
    private String name;
    private String description;
    private String activeIngredient;

    public PharmaceuticalProduct() {
        // Required for reconstruction
    }

    /**
     * Constructor for reconstruction from the persistence layer (Assembler).
     */
    public PharmaceuticalProduct(String id, String laboratoryId, String name, String description, String activeIngredient) {
        this.id = id;
        this.laboratoryId = laboratoryId;
        this.name = name;
        this.description = description;
        this.activeIngredient = activeIngredient;
    }

    /**
     * Constructor for PharmaceuticalProduct with a CreateProductCommand.
     * * @param command The CreateProductCommand.
     */
    public PharmaceuticalProduct(CreateProductCommand command) {
        this.id = UUID.randomUUID().toString();
        this.laboratoryId = Objects.requireNonNull(command.laboratoryId(), "Laboratory ID is required");
        this.name = Objects.requireNonNull(command.name(), "Product name is required");
        this.description = command.description();
        this.activeIngredient = Objects.requireNonNull(command.activeIngredient(), "Active ingredient is required");
    }
}