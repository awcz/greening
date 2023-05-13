package com.github.awcz.greening.services.onlinegame;

import com.github.awcz.greening.generated.onlinegame.Clan;

import java.util.Comparator;
import java.util.Objects;

class ClanComparator {

    private ClanComparator() {
    }

    static final Comparator<Clan> INSTANCE = getClanComparator();

    private static Comparator<Clan> getClanComparator() {
        return (o1, o2) -> {
            var points1 = o1.getPoints();
            var points2 = o2.getPoints();
            if (Objects.equals(points1, points2)) {
                return Float.compare(getPointsPerPlayer(o1), getPointsPerPlayer(o2));
            } else {
                return Integer.compare(points1, points2);
            }
        };
    }

    private static float getPointsPerPlayer(Clan clan) {
        return (float) clan.getPoints() / clan.getNumberOfPlayers();
    }
}
