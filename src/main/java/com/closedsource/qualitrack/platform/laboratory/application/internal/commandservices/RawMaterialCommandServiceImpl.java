package com.closedsource.qualitrack.platform.laboratory.application.internal.commandservices;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.RawMaterialCommandService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.RawMaterial;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateRawMaterialCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.LaboratoryRepository;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.RawMaterialRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service implementation that executes raw material commands.
 */
@Service
public class RawMaterialCommandServiceImpl implements RawMaterialCommandService {
    private final RawMaterialRepository rawMaterialRepository;
    private final LaboratoryRepository laboratoryRepository;

    public RawMaterialCommandServiceImpl(RawMaterialRepository rawMaterialRepository, LaboratoryRepository laboratoryRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    @Override
    public Result<String, ApplicationError> handle(CreateRawMaterialCommand command) {
        if (!laboratoryRepository.existsById(command.laboratoryId())) {
            return Result.failure(ApplicationError.notFound("Laboratory", command.laboratoryId()));
        }

        try {
            var rawMaterial = new RawMaterial(command);
            var savedMaterial = rawMaterialRepository.save(rawMaterial);
            return Result.success(savedMaterial.getId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("RawMaterial", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("create-raw-material", e.getMessage()));
        }
    }
}