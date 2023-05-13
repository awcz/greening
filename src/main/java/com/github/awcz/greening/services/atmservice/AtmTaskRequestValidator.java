package com.github.awcz.greening.services.atmservice;

import com.github.awcz.greening.generated.atmservice.Task;
import com.github.awcz.greening.errors.RequestValidationException;

import java.util.List;

import static java.util.Objects.isNull;

class AtmTaskRequestValidator {

    private AtmTaskRequestValidator() {
    }

    static void validate(List<Task> tasks) {
        if (tasks.stream().anyMatch(task -> isNull(task.getRegion()) || isNull(task.getRequestType()) || isNull(task.getAtmId()))) {
            throw new RequestValidationException("'region', 'requestType' and 'atmId'  cannot be null");
        }
    }
}
