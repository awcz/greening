package com.github.awcz.greening.services.onlinegame;

import com.github.awcz.greening.generated.onlinegame.Clan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClanComparatorTest {

    @Test
    void shouldCompareClans() {
        assertTrue(ClanComparator.INSTANCE.compare(
                new Clan().points(10).numberOfPlayers(10),
                new Clan().points(10).numberOfPlayers(11))
                >= 1);

        assertEquals(0, ClanComparator.INSTANCE.compare(
                new Clan().points(10).numberOfPlayers(10),
                new Clan().points(10).numberOfPlayers(10)));

        assertTrue(ClanComparator.INSTANCE.compare(
                new Clan().points(1).numberOfPlayers(10),
                new Clan().points(10).numberOfPlayers(10))
                <= -1);
    }
}