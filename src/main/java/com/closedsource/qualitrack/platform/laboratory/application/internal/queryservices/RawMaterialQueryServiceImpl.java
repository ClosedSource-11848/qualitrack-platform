package com.closedsource.qualitrack.platform.laboratory.application.internal.queryservices;

import com.closedsource.qualitrack.platform.laboratory.application.queryservices.RawMaterialQueryService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.RawMaterial;
import com.closedsource.qualitrack.platform.laboratory.domain.model.queries.GetLowStockMaterialsByLabIdQuery;
import com.closedsource.qualitrack.platform.laboratory.domain.model.queries.GetRawMaterialsByLabIdQuery;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.RawMaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service implementation that resolves raw material read queries.
 */
@Service
public class RawMaterialQueryServiceImpl implements RawMaterialQueryService {
    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterialQueryServiceImpl(RawMaterialRepository rawMaterialRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
    }

    @Override
    public List<RawMaterial> handle(GetRawMaterialsByLabIdQuery query) {
        return rawMaterialRepository.findAllByLaboratoryId(query.laboratoryId());
    }

    @Override
    public List<RawMaterial> handle(GetLowStockMaterialsByLabIdQuery query) {
        return rawMaterialRepository.findAllByLaboratoryId(query.laboratoryId())
                .stream()
                .filter(material -> material.getCurrentStock() < 100)
                .toList();
    }
}