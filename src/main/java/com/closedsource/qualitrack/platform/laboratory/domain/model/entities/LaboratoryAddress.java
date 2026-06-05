package com.closedsource.qualitrack.platform.laboratory.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * LaboratoryAddress domain entity.
 *
 * <p>
 * Represents the physical location of a laboratory.
 * Modeled as an entity within the Laboratory aggregate to allow its own
 * tracking and updates independently, while still belonging to a single Laboratory.
 * </p>
 */
@Getter
public class LaboratoryAddress {

    /**
     * The unique internal identifier for the address entity.
     */
    @Setter
    private Long id;

    /**
     * The street and number of the laboratory.
     */
    @Setter
    private String street;

    /**
     * The city where the laboratory is located.
     */
    @Setter
    private String city;

    /**
     * The postal or zip code of the laboratory.
     */
    @Setter
    private String zipCode;

    /**
     * Default constructor for LaboratoryAddress.
     * Required for reconstruction from persistence.
     */
    public LaboratoryAddress() {
        // Required for reconstruction
    }

    /**
     * Constructor for LaboratoryAddress with initial values.
     * @param street The street address.
     * @param city The city.
     * @param zipCode The zip code.
     */
    public LaboratoryAddress(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    /**
     * Updates the full address information with validation.
     * @param street The new street address.
     * @param city The new city.
     * @param zipCode The new zip code.
     */
    public void update(String street, String city, String zipCode) {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or blank");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or blank");
        }

        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    /**
     * Formats the full address as a single readable string.
     * @return The formatted address string.
     */
    public String getFullAddress() {
        return String.format("%s, %s, %s", this.street, this.city, this.zipCode);
    }
}