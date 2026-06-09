package com.closedsource.qualitrack.platform.batch.application.queryservices;

import com.closedsource.qualitrack.platform.batch.domain.model.entities.RawMaterialUsage;
import com.closedsource.qualitrack.platform.batch.domain.model.queries.GetRawMaterialUsageByBatchIdQuery;

import java.util.List;

/**
 * Application service contract for raw material usage read queries.
 */
public interface RawMaterialUsageQueryService {

    /**
     * Handles retrieval of all raw material usage records associated with a specific batch.
     *
     * @param query batch-id query
     * @return list of raw material usage records for the given batch
     * @see GetRawMaterialUsageByBatchIdQuery
     */
    List<RawMaterialUsage> handle(GetRawMaterialUsageByBatchIdQuery query);
}