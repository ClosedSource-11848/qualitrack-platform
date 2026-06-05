package com.closedsource.qualitrack.platform.laboratory.application.internal.commandservices;

import com.closedsource.qualitrack.platform.laboratory.application.commandservices.LaboratoryCommandService;
import com.closedsource.qualitrack.platform.laboratory.domain.model.aggregates.Laboratory;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.CreateLaboratoryCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.commands.UpdateLaboratoryCommand;
import com.closedsource.qualitrack.platform.laboratory.domain.model.valueobjects.LaboratoryName;
import com.closedsource.qualitrack.platform.laboratory.domain.repositories.LaboratoryRepository;
import com.closedsource.qualitrack.platform.shared.application.result.ApplicationError;
import com.closedsource.qualitrack.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Application service implementation that executes laboratory commands.
 */
@Service
public class LaboratoryCommandServiceImpl implements LaboratoryCommandService {
    private final LaboratoryRepository laboratoryRepository;

    public LaboratoryCommandServiceImpl(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    @Override
    public Result<String, ApplicationError> handle(CreateLaboratoryCommand command) {
        try {
            var name = new LaboratoryName(command.name());
            if (laboratoryRepository.existsByName(name)) {
                return Result.failure(ApplicationError.conflict(
                        "Laboratory",
                        "Laboratory with name '%s' already exists".formatted(command.name())
                ));
            }

            var laboratory = new Laboratory(command);
            var savedLaboratory = laboratoryRepository.save(laboratory);
            return Result.success(savedLaboratory.getId());

        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Laboratory", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("create-laboratory", e.getMessage()));
        }
    }

    @Override
    public Result<Laboratory, ApplicationError> handle(UpdateLaboratoryCommand command) {
        var result = laboratoryRepository.findById(command.laboratoryId());

        if (result.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Laboratory", command.laboratoryId()));
        }

        var laboratoryToUpdate = result.get();
        try {
            laboratoryToUpdate.updateProfile(command);
            var updatedLaboratory = laboratoryRepository.save(laboratoryToUpdate);
            return Result.success(updatedLaboratory);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Laboratory", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("update-laboratory", e.getMessage()));
        }
    }
}