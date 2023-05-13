package com.github.awcz.greening.services.onlinegame;

import com.github.awcz.greening.generated.onlinegame.Clan;
import com.github.awcz.greening.generated.onlinegame.Players;

import java.util.Random;
import java.util.stream.IntStream;

import static com.github.awcz.greening.utils.RandomGeneratorProvider.getRandomGenerator;

public class OnlineGameRequestGenerator {

    private static final Random RANDOM = getRandomGenerator();

    public Players generateRequest(int numberOfClans, int groupCount) {
        var clans = IntStream.range(0, numberOfClans)
                .mapToObj(value -> buildClan(groupCount)).toList();

        return new Players().clans(clans).groupCount(groupCount);
    }

    private Clan buildClan(int groupCount) {
        int maxPoints = 100_000;
        return new Clan()
                .numberOfPlayers(1 + RANDOM.nextInt(groupCount - 1))
                .points(1 + RANDOM.nextInt(maxPoints - 1));
    }
}