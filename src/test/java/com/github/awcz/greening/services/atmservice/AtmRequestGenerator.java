package com.github.awcz.greening.services.atmservice;

import com.github.awcz.greening.generated.atmservice.Task;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.github.awcz.greening.utils.RandomGeneratorProvider.getRandomGenerator;

public class AtmRequestGenerator {

    private static final List<Task.RequestTypeEnum> REQUEST_TYPE_ENUMS = List.of(Task.RequestTypeEnum.values());
    private static final Random RANDOM = getRandomGenerator();

    public List<Task> generateRequest(int count) {
        return IntStream.range(0, count).mapToObj(value -> buildTask()).toList();
    }

    private Task buildTask() {
        return new Task().region(1 + RANDOM.nextInt(9999)).requestType(getTaskType()).atmId(1 + RANDOM.nextInt(9999));
    }

    public static Task.RequestTypeEnum getTaskType() {
        return REQUEST_TYPE_ENUMS.get(RANDOM.nextInt(REQUEST_TYPE_ENUMS.size()));
    }
}