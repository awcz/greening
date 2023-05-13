package com.github.awcz.greening.services.onlinegame;

import com.github.awcz.greening.generated.onlinegame.Clan;
import com.github.awcz.greening.generated.onlinegame.Players;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import java.util.List;

@Controller
@ExecuteOn(TaskExecutors.IO)
class OnlineGameController {

    private final OnlineGameService onlineGameService;

    OnlineGameController(OnlineGameService onlineGameService) {
        this.onlineGameService = onlineGameService;
    }

    /**
     * Calculates the order of groups of players in an online game.
     *
     * @param players : list of clans and number of players in single group
     * @return : ordered list of groups
     */
    @Post("/onlinegame/calculate")
    List<List<Clan>> calculate(@Body @Valid Players players) {
        ClansRequestValidator.validate(players);
        return onlineGameService.calculateGroups(players);
    }
}
