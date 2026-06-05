package com.closedsource.qualitrack.platform.laboratory.domain.model.commands;

public record CreateLaboratoryCommand(
        String name,
        String regulationCode,
        String street,
        String city,
        String zipCode
) {}