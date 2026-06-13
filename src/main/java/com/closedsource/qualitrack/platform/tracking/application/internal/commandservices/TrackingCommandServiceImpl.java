package com.closedsource.qualitrack.platform.tracking.application.internal.commandservices;

import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import com.closedsource.qualitrack.platform.tracking.application.commandservices.TrackingCommandService;
import com.closedsource.qualitrack.platform.tracking.domain.model.commands.RecordMeasurementCommand;
import com.closedsource.qualitrack.platform.tracking.domain.model.commands.RecordTelemetryHistoryPointCommand;
import com.closedsource.qualitrack.platform.tracking.domain.model.commands.UpdateEquipmentTelemetryStatusCommand;
import com.closedsource.qualitrack.platform.tracking.domain.model.entities.EquipmentTelemetryStatus;
import com.closedsource.qualitrack.platform.tracking.domain.model.entities.Measurement;
import com.closedsource.qualitrack.platform.tracking.domain.model.entities.TelemetryHistoryPoint;
import com.closedsource.qualitrack.platform.tracking.domain.repositories.EquipmentTelemetryRepository;
import com.closedsource.qualitrack.platform.tracking.domain.repositories.EquipmentTelemetryStatusRepository;
import com.closedsource.qualitrack.platform.tracking.domain.repositories.MeasurementRepository;
import com.closedsource.qualitrack.platform.tracking.domain.repositories.TelemetryHistoryPointRepository;
import org.springframework.stereotype.Service;

/**
 * Application service implementation for Tracking write use cases.
 *
 * @remarks
 * Coordinates telemetry ingestion, status updates, and history persistence by
 * using domain repositories. It returns application-level Result objects so
 * REST controllers can map outcomes consistently.
 */
@Service
public class TrackingCommandServiceImpl implements TrackingCommandService {

    private final MeasurementRepository measurementRepository;
    private final EquipmentTelemetryStatusRepository statusRepository;
    private final TelemetryHistoryPointRepository historyPointRepository;
    private final EquipmentTelemetryRepository equipmentTelemetryRepository;

    /**
     * Creates a new TrackingCommandServiceImpl.
     *
     * @param measurementRepository repository for telemetry measurements
     * @param statusRepository repository for equipment telemetry statuses
     * @param historyPointRepository repository for telemetry history points
     * @param equipmentTelemetryRepository repository for equipment telemetry aggregates
     */
    public TrackingCommandServiceImpl(
            MeasurementRepository measurementRepository,
            EquipmentTelemetryStatusRepository statusRepository,
            TelemetryHistoryPointRepository historyPointRepository,
            EquipmentTelemetryRepository equipmentTelemetryRepository
    ) {
        this.measurementRepository = measurementRepository;
        this.statusRepository = statusRepository;
        this.historyPointRepository = historyPointRepository;
        this.equipmentTelemetryRepository = equipmentTelemetryRepository;
    }

    /**
     * Records a new telemetry measurement.
     *
     * @param command the record measurement command
     * @return the recorded measurement identifier or an application error
     */
    @Override
    public Result<Long, ApplicationError> handle(RecordMeasurementCommand command) {
        try {
            ensureTelemetryAggregateExists(command.equipmentId());

            var measurement = Measurement.record(
                    command.equipmentId(),
                    command.parameterName(),
                    command.value(),
                    command.unit(),
                    command.timestamp()
            );

            var savedMeasurement = measurementRepository.save(measurement);

            return Result.success(savedMeasurement.getId());
        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("measurement", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected(
                    "record measurement",
                    exception.getMessage()
            ));
        }
    }

    /**
     * Records a new telemetry history point.
     *
     * @param command the record telemetry history point command
     * @return the recorded history point identifier or an application error
     */
    @Override
    public Result<Long, ApplicationError> handle(RecordTelemetryHistoryPointCommand command) {
        try {
            ensureTelemetryAggregateExists(command.equipmentId());

            var historyPoint = TelemetryHistoryPoint.record(
                    command.equipmentId(),
                    command.parameterName(),
                    command.recordedValue(),
                    command.timestamp(),
                    command.isAnomaly()
            );

            var savedHistoryPoint = historyPointRepository.save(historyPoint);

            return Result.success(savedHistoryPoint.getId());
        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("telemetry history point", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected(
                    "record telemetry history point",
                    exception.getMessage()
            ));
        }
    }

    /**
     * Updates the current telemetry status for equipment.
     *
     * @param command the update equipment telemetry status command
     * @return the saved status identifier or an application error
     */
    @Override
    public Result<Long, ApplicationError> handle(UpdateEquipmentTelemetryStatusCommand command) {
        try {
            ensureTelemetryAggregateExists(command.equipmentId());

            var status = EquipmentTelemetryStatus.update(
                    command.equipmentId(),
                    command.isOnline(),
                    command.currentStatus(),
                    command.lastHeartbeat()
            );

            var savedStatus = statusRepository.save(status);

            return Result.success(savedStatus.getId());
        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("equipment telemetry status", exception.getMessage()));
        } catch (Exception exception) {
            return Result.failure(ApplicationError.unexpected(
                    "update equipment telemetry status",
                    exception.getMessage()
            ));
        }
    }

    private void ensureTelemetryAggregateExists(Long equipmentId) {
        if (!equipmentTelemetryRepository.existsByEquipmentId(equipmentId)) {
            equipmentTelemetryRepository.save(
                    com.closedsource.qualitrack.platform.tracking.domain.model.aggregates.EquipmentTelemetry
                            .createForEquipment(equipmentId)
            );
        }
    }
}