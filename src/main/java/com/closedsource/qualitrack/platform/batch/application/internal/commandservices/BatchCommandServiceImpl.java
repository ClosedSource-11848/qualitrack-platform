package com.closedsource.qualitrack.platform.batch.application.internal.commandservices;

import com.closedsource.qualitrack.platform.batch.application.commandservices.BatchCommandService;
import com.closedsource.qualitrack.platform.batch.application.internal.outboundservices.acl.ExternalLaboratoryService;
import com.closedsource.qualitrack.platform.batch.domain.model.aggregates.Batch;
import com.closedsource.qualitrack.platform.batch.domain.model.commands.CreateBatchCommand;
import com.closedsource.qualitrack.platform.batch.domain.model.commands.ReleaseBatchCommand;
import com.closedsource.qualitrack.platform.batch.domain.model.commands.RejectBatchCommand;
import com.closedsource.qualitrack.platform.batch.domain.repositories.BatchRepository;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.ProductRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service implementation that executes batch commands.
 *
 * <p>Handles the orchestration of creating, releasing, and rejecting production batches,
 * enforcing business rules such as laboratory existence, product existence, and unique
 * batch numbers.</p>
 */
@Service
public class BatchCommandServiceImpl implements BatchCommandService {

    private final BatchRepository batchRepository;
    private final ExternalLaboratoryService externalLaboratoryService;
    private final ProductRepository productRepository;

    public BatchCommandServiceImpl(BatchRepository batchRepository,
                                   ExternalLaboratoryService externalLaboratoryService,
                                   ProductRepository productRepository) {
        this.batchRepository = batchRepository;
        this.externalLaboratoryService = externalLaboratoryService;
        this.productRepository = productRepository;
    }

    @Override
    public Result<Long, ApplicationError> handle(CreateBatchCommand command) {
        if (!externalLaboratoryService.existsLaboratoryById(command.labId())) {
            return Result.failure(ApplicationError.notFound(
                    "Laboratory",
                    String.valueOf(command.labId())
            ));
        }

        var productResult = productRepository.findById(command.productId());

        if (productResult.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "PharmaceuticalProduct",
                    String.valueOf(command.productId())
            ));
        }

        if (batchRepository.existsByBatchNumber(command.batchNumber())) {
            return Result.failure(ApplicationError.conflict(
                    "Batch",
                    "Batch with number '%s' already exists".formatted(command.batchNumber())
            ));
        }

        try {
            var product = productResult.get();

            var batch = new Batch(
                    command,
                    product.getName(),
                    "units"
            );

            var savedBatch = batchRepository.save(batch);

            return Result.success(savedBatch.getId());

        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Batch", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("create-batch", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(ReleaseBatchCommand command) {
        var batchResult = batchRepository.findById(command.batchId());

        if (batchResult.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "Batch",
                    String.valueOf(command.batchId())
            ));
        }

        try {
            var batch = batchResult.get();

            batch.release(command);

            var updatedBatch = batchRepository.save(batch);

            return Result.success(updatedBatch.getId());

        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.failure(ApplicationError.validationError("Batch", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("release-batch", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(RejectBatchCommand command) {
        var batchResult = batchRepository.findById(command.batchId());

        if (batchResult.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "Batch",
                    String.valueOf(command.batchId())
            ));
        }

        try {
            var batch = batchResult.get();

            batch.reject(command);

            var updatedBatch = batchRepository.save(batch);

            return Result.success(updatedBatch.getId());

        } catch (IllegalArgumentException | IllegalStateException e) {
            return Result.failure(ApplicationError.validationError("Batch", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("reject-batch", e.getMessage()));
        }
    }
}