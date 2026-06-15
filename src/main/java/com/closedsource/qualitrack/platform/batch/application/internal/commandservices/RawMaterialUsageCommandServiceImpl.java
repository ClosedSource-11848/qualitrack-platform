package com.closedsource.qualitrack.platform.batch.application.internal.commandservices;

import com.closedsource.qualitrack.platform.batch.application.commandservices.RawMaterialUsageCommandService;
import com.closedsource.qualitrack.platform.batch.domain.model.commands.LinkRawMaterialCommand;
import com.closedsource.qualitrack.platform.batch.domain.model.entities.RawMaterialUsage;
import com.closedsource.qualitrack.platform.batch.domain.model.events.RawMaterialLinkedToBatchEvent;
import com.closedsource.qualitrack.platform.batch.domain.repositories.BatchRepository;
import com.closedsource.qualitrack.platform.batch.domain.repositories.RawMaterialUsageRepository;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.RawMaterialRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Application service implementation that executes raw material usage commands.
 *
 * <p>Handles the orchestration of linking raw materials to production batches,
 * enforcing business rules such as batch existence and raw material existence.</p>
 */
@Service
public class RawMaterialUsageCommandServiceImpl implements RawMaterialUsageCommandService {

    private final RawMaterialUsageRepository rawMaterialUsageRepository;
    private final BatchRepository batchRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RawMaterialUsageCommandServiceImpl(RawMaterialUsageRepository rawMaterialUsageRepository,
                                              BatchRepository batchRepository,
                                              RawMaterialRepository rawMaterialRepository,
                                              ApplicationEventPublisher eventPublisher) {
        this.rawMaterialUsageRepository = rawMaterialUsageRepository;
        this.batchRepository = batchRepository;
        this.rawMaterialRepository = rawMaterialRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Long, ApplicationError> handle(LinkRawMaterialCommand command) {
        if (!batchRepository.existsById(command.batchId())) {
            return Result.failure(ApplicationError.notFound(
                    "Batch",
                    String.valueOf(command.batchId())
            ));
        }

        var rawMaterialResult = rawMaterialRepository.findById(command.rawMaterialId());

        if (rawMaterialResult.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "RawMaterial",
                    String.valueOf(command.rawMaterialId())
            ));
        }

        try {
            var rawMaterial = rawMaterialResult.get();

            var usage = new RawMaterialUsage(
                    command,
                    rawMaterial.getName(),
                    rawMaterial.getUnit(),
                    LocalDate.now().toString()
            );

            var savedUsage = rawMaterialUsageRepository.save(usage);

            eventPublisher.publishEvent(RawMaterialLinkedToBatchEvent.from(savedUsage));

            return Result.success(savedUsage.getId());

        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("RawMaterialUsage", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("link-raw-material", e.getMessage()));
        }
    }
}