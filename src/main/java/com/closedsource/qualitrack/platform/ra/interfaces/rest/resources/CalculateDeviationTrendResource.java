package com.closedsource.qualitrack.platform.ra.interfaces.rest.resources;

/**
 * Resource used to request deviation trend calculation.
 *
 * @param equipmentId The equipment identifier.
 * @param parameterName The monitored parameter name.
 */
public record CalculateDeviationTrendResource(
        Long equipmentId,
        String parameterName
) {
}