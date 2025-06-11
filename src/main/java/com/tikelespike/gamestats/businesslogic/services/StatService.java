package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.PlayerStatistics;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class providing statistical data.
 */
@Service
public class StatService {

    private final PlayerService playerService;
    private final GameService gameService;

    /**
     * Creates a new statistics service. This is usually done by the Spring framework, which manages the service's
     * lifecycle and injects the required dependencies.
     *
     * @param playerService service for managing players
     * @param gameService service for managing games
     */
    public StatService(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    /**
     * Retrieves the statistics of all players in the application.
     *
     * @return a list of player statistics
     */
    public List<PlayerStatistics> getAllPlayerStatistics() {
        List<Player> players = playerService.getAllPlayers();
        List<Game> games = gameService.getAllGames();

        return players.stream().map(player -> calculatePlayerStatistics(player, games)).toList();
    }

    private PlayerStatistics calculatePlayerStatistics(Player player, List<Game> games) {
        PlayerStatistics statistics = new PlayerStatistics(player);
        games.forEach(statistics::addGame);
        return statistics;
    }
}
