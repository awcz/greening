package com.github.awcz.greening.utils;

import java.util.Random;

public class RandomGeneratorProvider {

    private static final RandomMode DEFAULT_RANDOM_MODE = RandomMode.CONSTANT_SEED;

    public static Random getRandomGenerator() {
        return getRandomGenerator(DEFAULT_RANDOM_MODE);
    }

    public static Random getRandomGenerator(RandomMode mode) {
        switch (mode) {
            case CONSTANT_SEED -> {
                int seed = 1234;
                return new Random(seed);
            }
            case RANDOM -> {
                return new Random();
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public enum RandomMode {
        CONSTANT_SEED, // to get reproducible results
        RANDOM // to generate different cases each time
    }
}