package com.github.awcz.greening.services.atmservice;

import com.github.awcz.greening.generated.atmservice.ATM;
import com.github.awcz.greening.generated.atmservice.Task;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import java.util.List;

@Controller
@ExecuteOn(TaskExecutors.IO)
class AtmController {

    private final AtmService atmService;

    AtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    /**
     * Calculates ATMs order for service team.
     *
     * @param task : list of service tasks
     * @return : ordered list of ATMs to service
     */
    @Post("/atms/calculateOrder")
    List<ATM> calculateOrder(@Body @Valid List<Task> task) {
        AtmTaskRequestValidator.validate(task);
        return atmService.calculate(task);
    }
}
