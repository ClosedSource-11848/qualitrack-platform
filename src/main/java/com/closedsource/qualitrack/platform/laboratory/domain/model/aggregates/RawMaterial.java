package com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates;

import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateRawMaterialCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.events.RawMaterialLowStockEvent;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.StockQuantity;
import com.closedsource.qualitrack.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * RawMaterial aggregate root.
 *
 * <p>Represents raw materials used for production in a laboratory.
 * It governs stock levels and triggers compliance alerts when stock falls below safety thresholds.</p>
 */
public class RawMaterial extends AbstractDomainAggregateRoot<RawMaterial> {

    @Getter
    @Setter
    private String id;

    @Getter
    private String laboratoryId;

    @Getter
    private String name;

    @Getter
    private String unit;

    private StockQuantity stockQuantity;

    // Safety threshold set by business rules (could be dynamic in a real scenario)
    private final int minimumThreshold = 100;

    public RawMaterial() {
        // Required for reconstruction
    }

    /**
     * Constructor for reconstruction from the persistence layer (Assembler).
     */
    public RawMaterial(String id, String laboratoryId, String name, String unit, int currentStock) {
        this.id = id;
        this.laboratoryId = laboratoryId;
        this.name = name;
        this.unit = unit;
        this.stockQuantity = new StockQuantity(currentStock);
    }

    /**
     * Constructor for RawMaterial with a CreateRawMaterialCommand.
     * * @param command The CreateRawMaterialCommand.
     */
    public RawMaterial(CreateRawMaterialCommand command) {
        this.id = UUID.randomUUID().toString();
        this.laboratoryId = Objects.requireNonNull(command.laboratoryId(), "Laboratory ID is required");
        this.name = Objects.requireNonNull(command.name(), "Material name is required");
        this.unit = Objects.requireNonNull(command.unit(), "Unit is required");
        this.stockQuantity = new StockQuantity(command.initialStock() != null ? command.initialStock() : 0);
    }

    /**
     * Gets the current stock value.
     */
    public int getCurrentStock() {
        return this.stockQuantity.value();
    }

    /**
     * Adds quantity to the current stock.
     */
    public void addStock(int amount) {
        this.stockQuantity = this.stockQuantity.add(amount);
    }

    /**
     * Consumes quantity from the current stock.
     * Registers a RawMaterialLowStockEvent if the stock drops below the minimum threshold.
     */
    public void consumeStock(int amount) {
        this.stockQuantity = this.stockQuantity.subtract(amount);

        if (this.stockQuantity.value() < minimumThreshold) {
            this.registerDomainEvent(new RawMaterialLowStockEvent(
                    this.id,
                    this.laboratoryId,
                    this.name,
                    this.stockQuantity.value(),
                    this.minimumThreshold
            ));
        }
    }
}