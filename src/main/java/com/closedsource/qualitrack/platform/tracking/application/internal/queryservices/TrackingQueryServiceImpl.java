package com.closedsource.qualitrack.platform.tracking.application.internal.queryservices;

import com.closedsource.qualitrack.platform.tracking.application.queryservices.TrackingQueryService;
import com.closedsource.qualitrack.platform.tracking.domain.model.entities.EquipmentTelemetryStatus;
import com.closedsource.qualitrack.platform.tracking.domain.model.entities.Measurement;
import com.closedsource.qualitrack.platform.tracking.domain.model.entities.TelemetryHistoryPoint;
import com.closedsource.qualitrack.platform.tracking.domain.model.queries.GetEquipmentTelemetryStatusByEquipmentIdQuery;
import com.closedsource.qualitrack.platform.tracking.domain.model.queries.GetLatestMeasurementsQuery;
import com.closedsource.qualitrack.platform.tracking.domain.model.queries.GetTelemetryHistoryQuery;
import com.closedsource.qualitrack.platform.tracking.domain.repositories.EquipmentTelemetryStatusRepository;
import com.closedsource.qualitrack.platform.tracking.domain.repositories.MeasurementRepository;
import com.closedsource.qualitrack.platform.tracking.domain.repositories.TelemetryHistoryPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementation for Tracking read use cases.
 *
 * @remarks
 * Coordinates read-only queries for latest telemetry measurements, equipment
 * telemetry status, and historical telemetry points.
 */
@Service
public class TrackingQueryServiceImpl implements TrackingQueryService {

    private final MeasurementRepository measurementRepository;
    private final EquipmentTelemetryStatusRepository statusRepository;
    private final TelemetryHistoryPointRepository historyPointRepository;

    /**
     * Creates a new TrackingQueryServiceImpl.
     *
     * @param measurementRepository repository for telemetry measurements
     * @param statusRepository repository for equipment telemetry statuses
     * @param historyPointRepository repository for telemetry history points
     */
    public TrackingQueryServiceImpl(
            MeasurementRepository measurementRepository,
            EquipmentTelemetryStatusRepository statusRepository,
            TelemetryHistoryPointRepository historyPointRepository
    ) {
        this.measurementRepository = measurementRepository;
        this.statusRepository = statusRepository;
        this.historyPointRepository = historyPointRepository;
    }

    /**
     * Retrieves latest telemetry measurements.
     *
     * @param query the latest measurements query
     * @return latest telemetry measurements
     */
    @Override
    public List<Measurement> handle(GetLatestMeasurementsQuery query) {
        if (query.equipmentId() != null) {
            return measurementRepository.findLatestByEquipmentId(query.equipmentId());
        }

        return measurementRepository.findLatest();
    }

    /**
     * Retrieves the latest telemetry status for an equipment.
     *
     * @param query the equipment telemetry status query
     * @return latest telemetry status when found
     */
    @Override
    public Optional<EquipmentTelemetryStatus> handle(
            GetEquipmentTelemetryStatusByEquipmentIdQuery query
    ) {
        return statusRepository.findLatestByEquipmentId(query.equipmentId());
    }

    /**
     * Retrieves telemetry history points matching the query filters.
     *
     * @param query the telemetry history query
     * @return telemetry history points
     */
    @Override
    public List<TelemetryHistoryPoint> handle(GetTelemetryHistoryQuery query) {
        return historyPointRepository.findByFilters(
                query.equipmentId(),
                query.from(),
                query.to()
        );
    }
}