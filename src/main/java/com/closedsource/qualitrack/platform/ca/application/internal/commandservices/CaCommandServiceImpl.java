package com.closedsource.qualitrack.platform.ca.application.internal.commandservices;

import com.closedsource.qualitrack.platform.ca.application.commandservices.CaCommandService;
import com.closedsource.qualitrack.platform.ca.application.internal.outboundservices.acl.CaExternalBatchService;
import com.closedsource.qualitrack.platform.ca.application.internal.outboundservices.acl.CaExternalEquipmentService;
import com.closedsource.qualitrack.platform.ca.domain.model.aggregates.DeviationAlert;
import com.closedsource.qualitrack.platform.ca.domain.model.commands.AcknowledgeAlertCommand;
import com.closedsource.qualitrack.platform.ca.domain.model.commands.CreateDeviationAlertCommand;
import com.closedsource.qualitrack.platform.ca.domain.model.commands.ResolveAlertCommand;
import com.closedsource.qualitrack.platform.ca.domain.model.commands.UpdateNotificationPreferenceCommand;
import com.closedsource.qualitrack.platform.ca.domain.model.entities.ComplianceEvent;
import com.closedsource.qualitrack.platform.ca.domain.model.entities.NotificationPreference;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.ComplianceEventType;
import com.closedsource.qualitrack.platform.ca.domain.repositories.ComplianceEventRepository;
import com.closedsource.qualitrack.platform.ca.domain.repositories.DeviationAlertRepository;
import com.closedsource.qualitrack.platform.ca.domain.repositories.NotificationPreferenceRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Application service implementation that executes CA commands.
 *
 * <p>Handles the orchestration of creating, acknowledging, and resolving deviation
 * alerts, as well as updating user notification preferences. It also records
 * compliance events for traceability.</p>
 */
@Service
public class CaCommandServiceImpl implements CaCommandService {

    private final DeviationAlertRepository deviationAlertRepository;
    private final ComplianceEventRepository complianceEventRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    private final CaExternalEquipmentService caExternalEquipmentService;
    private final CaExternalBatchService caExternalBatchService;

    public CaCommandServiceImpl(
            DeviationAlertRepository deviationAlertRepository,
            ComplianceEventRepository complianceEventRepository,
            NotificationPreferenceRepository notificationPreferenceRepository,
            CaExternalEquipmentService caExternalEquipmentService,
            CaExternalBatchService caExternalBatchService
    ) {
        this.deviationAlertRepository = deviationAlertRepository;
        this.complianceEventRepository = complianceEventRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
        this.caExternalEquipmentService = caExternalEquipmentService;
        this.caExternalBatchService = caExternalBatchService;
    }

    @Override
    public Result<Long, ApplicationError> handle(CreateDeviationAlertCommand command) {
        if (!caExternalEquipmentService.existsEquipmentById(command.equipmentId())) {
            return Result.failure(ApplicationError.notFound(
                    "Equipment",
                    String.valueOf(command.equipmentId())
            ));
        }

        if (command.batchId() != null && !caExternalBatchService.existsBatchById(command.batchId())) {
            return Result.failure(ApplicationError.notFound(
                    "Batch",
                    String.valueOf(command.batchId())
            ));
        }

        try {
            var alert = new DeviationAlert(command);
            var savedAlert = deviationAlertRepository.save(alert);

            complianceEventRepository.save(new ComplianceEvent(
                    savedAlert.getId(),
                    ComplianceEventType.DEVIATION_ALERT_CREATED,
                    "Deviation alert created for parameter '%s'.".formatted(savedAlert.getParameterName()),
                    Instant.now().toString(),
                    null
            ));

            return Result.success(savedAlert.getId());

        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("DeviationAlert", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("create-deviation-alert", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(AcknowledgeAlertCommand command) {
        var alertResult = deviationAlertRepository.findById(command.alertId());

        if (alertResult.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "DeviationAlert",
                    String.valueOf(command.alertId())
            ));
        }

        try {
            var alert = alertResult.get();

            alert.acknowledge(command);

            var updatedAlert = deviationAlertRepository.save(alert);

            complianceEventRepository.save(new ComplianceEvent(
                    updatedAlert.getId(),
                    ComplianceEventType.DEVIATION_ALERT_ACKNOWLEDGED,
                    "Deviation alert acknowledged.",
                    Instant.now().toString(),
                    updatedAlert.getAcknowledgedBy()
            ));

            return Result.success(updatedAlert.getId());

        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.failure(ApplicationError.validationError("DeviationAlert", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("acknowledge-alert", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(ResolveAlertCommand command) {
        var alertResult = deviationAlertRepository.findById(command.alertId());

        if (alertResult.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "DeviationAlert",
                    String.valueOf(command.alertId())
            ));
        }

        try {
            var alert = alertResult.get();

            alert.resolve(command);

            var updatedAlert = deviationAlertRepository.save(alert);

            complianceEventRepository.save(new ComplianceEvent(
                    updatedAlert.getId(),
                    ComplianceEventType.DEVIATION_ALERT_RESOLVED,
                    "Deviation alert resolved: %s".formatted(updatedAlert.getResolutionNotes()),
                    Instant.now().toString(),
                    updatedAlert.getResolvedBy()
            ));

            return Result.success(updatedAlert.getId());

        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.failure(ApplicationError.validationError("DeviationAlert", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("resolve-alert", e.getMessage()));
        }
    }

    @Override
    public Result<NotificationPreference, ApplicationError> handle(UpdateNotificationPreferenceCommand command) {
        try {
            var preference = notificationPreferenceRepository.findByUserId(command.userId())
                    .orElseGet(() -> new NotificationPreference(command.userId()));

            preference.update(command);

            var updatedPreference = notificationPreferenceRepository.save(preference);

            complianceEventRepository.save(new ComplianceEvent(
                    updatedPreference.getUserId(),
                    ComplianceEventType.NOTIFICATION_PREFERENCE_UPDATED,
                    "Notification preferences updated.",
                    Instant.now().toString(),
                    updatedPreference.getUserId()
            ));

            return Result.success(updatedPreference);

        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("NotificationPreference", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("update-notification-preference", e.getMessage()));
        }
    }
}