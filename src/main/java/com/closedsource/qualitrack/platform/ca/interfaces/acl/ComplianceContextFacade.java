package com.closedsource.qualitrack.platform.ca.interfaces.acl;

import com.closedsource.qualitrack.platform.ca.domain.model.entities.ComplianceEvent;
import com.closedsource.qualitrack.platform.ca.domain.model.valueobjects.ComplianceEventType;
import com.closedsource.qualitrack.platform.ca.domain.repositories.ComplianceEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * ACL facade exposed by the Compliance & Alerts bounded context.
 *
 * <p>Provides a stable interface for other bounded contexts to interact with CA
 * without depending on CA internal application services or domain commands.</p>
 */
@Service
public class ComplianceContextFacade {

    private final ComplianceEventRepository complianceEventRepository;

    public ComplianceContextFacade(ComplianceEventRepository complianceEventRepository) {
        this.complianceEventRepository = complianceEventRepository;
    }

    /**
     * Records a raw material low stock compliance event.
     *
     * @param rawMaterialId The raw material identifier.
     * @param laboratoryId The laboratory identifier.
     * @param materialName The raw material name.
     * @param currentStock The current stock quantity.
     * @param minimumThreshold The minimum required stock quantity.
     */
    public void recordRawMaterialLowStockEvent(
            Long rawMaterialId,
            Long laboratoryId,
            String materialName,
            Integer currentStock,
            Integer minimumThreshold
    ) {
        complianceEventRepository.save(new ComplianceEvent(
                rawMaterialId,
                ComplianceEventType.RAW_MATERIAL_LOW_STOCK,
                "Raw material '%s' from laboratory ID %d is below minimum stock. Current stock: %d, minimum threshold: %d."
                        .formatted(materialName, laboratoryId, currentStock, minimumThreshold),
                Instant.now().toString(),
                null
        ));
    }
}