package com.closedsource.qualitrack.platform.laboratory.domain.model.commands;

public record CreateProductCommand(
        String laboratoryId,
        String name,
        String description,
        String activeIngredient
) {}