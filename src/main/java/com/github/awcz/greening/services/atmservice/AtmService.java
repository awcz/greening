package com.github.awcz.greening.services.atmservice;

import com.github.awcz.greening.generated.atmservice.ATM;
import com.github.awcz.greening.generated.atmservice.Task;
import jakarta.inject.Singleton;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.github.awcz.greening.generated.atmservice.Task.RequestTypeEnum.*;
import static java.util.Comparator.comparing;

@Singleton
class AtmService {

    private final Map<Task.RequestTypeEnum, Integer> priorities;

    AtmService() {
        priorities = new EnumMap<>(Task.RequestTypeEnum.class);
        priorities.put(FAILURE_RESTART, 1);
        priorities.put(PRIORITY, 2);
        priorities.put(SIGNAL_LOW, 3);
        priorities.put(STANDARD, 4);
    }

    public List<ATM> calculate(List<Task> task) {
        return task.stream()
                .sorted(comparing(t -> priorities.get(t.getRequestType())))
                .sorted(comparing(Task::getRegion))
                .map(this::toAtm)
                .distinct()
                .toList();
    }

    private ATM toAtm(Task task) {
        return new ATM()
                .atmId(task.getAtmId())
                .region(task.getRegion());
    }
}
