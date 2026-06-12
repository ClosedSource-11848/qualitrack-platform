package com.closedsource.qualitrack.platform.ra.infrastructure.persistence.jpa.entities;

import com.closedsource.qualitrack.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity representing a KPI dashboard snapshot.
 */
@Entity
@Table(name = "kpi_dashboards")
@Getter
@Setter
@NoArgsConstructor
public class KpiDashboardPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "laboratory_id", nullable = false)
    private Long laboratoryId;

    @Column(nullable = false, length = 30)
    private String timestamp;

    @Column(name = "overall_health_score", nullable = false)
    private Double overallHealthScore;
}