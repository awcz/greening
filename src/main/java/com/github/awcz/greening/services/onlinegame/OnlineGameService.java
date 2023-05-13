package com.github.awcz.greening.services.onlinegame;

import com.github.awcz.greening.generated.onlinegame.Clan;
import com.github.awcz.greening.generated.onlinegame.Players;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.reverseOrder;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;

@Singleton
class OnlineGameService {

    List<List<Clan>> calculateGroups(Players players) {
        var clans = requireNonNull(players.getClans())
                .stream()
                .sorted(ClanComparator.INSTANCE.reversed())
                .collect(toCollection(ArrayList::new));
        List<List<Clan>> groups = new ArrayList<>();
        while (!clans.isEmpty()) {
            groups.add(buildGroup(players, clans));
        }
        return groups;
    }

    private List<Clan> buildGroup(Players players, List<Clan> clans) {
        List<Clan> group = new ArrayList<>();
        List<Integer> clansIndexesToRemove = new ArrayList<>();
        int groupCurrentSize = 0;
        for (int i = 0; i < clans.size(); i++) {
            var clan = clans.get(i);
            boolean matchingClanSize = players.getGroupCount() - clan.getNumberOfPlayers() - groupCurrentSize >= 0;
            if (matchingClanSize && groupCurrentSize < players.getGroupCount()) {
                clansIndexesToRemove.add(i);
                group.add(clan);
                groupCurrentSize += clan.getNumberOfPlayers();
            }
        }
        for (int index : clansIndexesToRemove.stream().sorted((reverseOrder())).toList()) {
            clans.remove(index);
        }
        return group;
    }
}
