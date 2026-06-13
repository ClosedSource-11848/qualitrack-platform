package com.closedsource.qualitrack.platform.tracking.interfaces.rest.resources;

/**
 * Resource used to request recording a telemetry measurement.
 *
 * @param equipmentId the equipment that produced the measurement
 * @param parameterName the monitored parameter name
 * @param value the measured value
 * @param unit the measurement unit
 * @param timestamp the source timestamp
 */
public record RecordMeasurementResource(
        Long equipmentId,
        String parameterName,
        Double value,
        String unit,
        String timestamp
) {
}