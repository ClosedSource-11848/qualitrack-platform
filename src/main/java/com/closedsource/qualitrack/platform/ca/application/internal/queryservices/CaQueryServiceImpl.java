package com.closedsource.qualitrack.platform.ca.application.internal.queryservices;

import com.closedsource.qualitrack.platform.ca.application.queryservices.CaQueryService;
import com.closedsource.qualitrack.platform.ca.domain.model.aggregates.DeviationAlert;
import com.closedsource.qualitrack.platform.ca.domain.model.entities.ComplianceEvent;
import com.closedsource.qualitrack.platform.ca.domain.model.entities.NotificationPreference;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetAlertByIdQuery;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetAlertsQuery;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetComplianceEventsByRelatedEntityIdQuery;
import com.closedsource.qualitrack.platform.ca.domain.model.queries.GetNotificationPreferenceByUserIdQuery;
import com.closedsource.qualitrack.platform.ca.domain.repositories.ComplianceEventRepository;
import com.closedsource.qualitrack.platform.ca.domain.repositories.DeviationAlertRepository;
import com.closedsource.qualitrack.platform.ca.domain.repositories.NotificationPreferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementation that executes CA read queries.
 *
 * <p>Handles retrieval of deviation alerts, compliance events, and notification
 * preferences using the domain repository ports.</p>
 */
@Service
public class CaQueryServiceImpl implements CaQueryService {

    private final DeviationAlertRepository deviationAlertRepository;
    private final ComplianceEventRepository complianceEventRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;

    public CaQueryServiceImpl(
            DeviationAlertRepository deviationAlertRepository,
            ComplianceEventRepository complianceEventRepository,
            NotificationPreferenceRepository notificationPreferenceRepository
    ) {
        this.deviationAlertRepository = deviationAlertRepository;
        this.complianceEventRepository = complianceEventRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
    }

    @Override
    public List<DeviationAlert> handle(GetAlertsQuery query) {
        if (query.equipmentId() != null && query.status() != null) {
            return deviationAlertRepository.findAllByEquipmentIdAndStatus(
                    query.equipmentId(),
                    query.status()
            );
        }

        if (query.batchId() != null && query.status() != null) {
            return deviationAlertRepository.findAllByBatchIdAndStatus(
                    query.batchId(),
                    query.status()
            );
        }

        if (query.equipmentId() != null) {
            return deviationAlertRepository.findAllByEquipmentId(query.equipmentId());
        }

        if (query.batchId() != null) {
            return deviationAlertRepository.findAllByBatchId(query.batchId());
        }

        if (query.status() != null) {
            return deviationAlertRepository.findAllByStatus(query.status());
        }

        if (query.severity() != null) {
            return deviationAlertRepository.findAllBySeverity(query.severity());
        }

        return deviationAlertRepository.findAll();
    }

    @Override
    public Optional<DeviationAlert> handle(GetAlertByIdQuery query) {
        return deviationAlertRepository.findById(query.alertId());
    }

    @Override
    public List<ComplianceEvent> handle(GetComplianceEventsByRelatedEntityIdQuery query) {
        return complianceEventRepository.findAllByRelatedEntityId(query.relatedEntityId());
    }

    @Override
    public Optional<NotificationPreference> handle(GetNotificationPreferenceByUserIdQuery query) {
        return notificationPreferenceRepository.findByUserId(query.userId());
    }
}