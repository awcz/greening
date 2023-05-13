package com.github.awcz.greening.services.onlinegame;

import com.github.awcz.greening.generated.onlinegame.Players;
import com.github.awcz.greening.errors.RequestValidationException;

class ClansRequestValidator {

    private ClansRequestValidator() {
    }

    static void validate(Players players) {
        if (players.getGroupCount() == null) {
            throw new RequestValidationException("groupCount cannot be null");
        }
        var groupCount = players.getGroupCount();
        if (players.getClans() == null || !players.getClans().stream().allMatch(v -> v.getNumberOfPlayers() != null
                && v.getNumberOfPlayers() >= 1
                && v.getNumberOfPlayers() <= 1000
                && v.getNumberOfPlayers() <= groupCount)) {
            throw new RequestValidationException("Clan size cannot be null, should be in range <1,1000> and less or equal 'groupCount'");
        }
        if (!players.getClans().stream().allMatch(v -> v.getPoints() != null && v.getPoints() >= 1 && v.getPoints() <= 100_000)) {
            throw new RequestValidationException("Clan points should be in range <1,100000>");
        }
    }
}
