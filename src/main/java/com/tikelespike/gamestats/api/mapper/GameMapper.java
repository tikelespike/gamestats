package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.AlignmentDTO;
import com.tikelespike.gamestats.api.entities.GameDTO;
import com.tikelespike.gamestats.api.entities.PlayerParticipationDTO;
import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.services.PlayerService;
import com.tikelespike.gamestats.businesslogic.services.ScriptService;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Maps between game business objects and their REST transfer representation.
 */
@Component
public class GameMapper extends Mapper<Game, GameDTO> {

    private final ScriptService scriptService;
    private final PlayerService playerService;
    private final Mapper<Alignment, AlignmentDTO> alignmentMapper;
    private final Mapper<PlayerParticipation, PlayerParticipationDTO> playerParticipationMapper;

    /**
     * Creates a new game mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param scriptService service to load scripts from ids
     * @param playerService service to load players from ids
     * @param alignmentMapper mapper for alignments
     * @param playerParticipationMapper mapper for player participations
     */
    public GameMapper(ScriptService scriptService, PlayerService playerService,
                      AlignmentMapper alignmentMapper, PlayerParticipationMapper playerParticipationMapper) {
        this.scriptService = scriptService;
        this.playerService = playerService;
        this.alignmentMapper = alignmentMapper;
        this.playerParticipationMapper = playerParticipationMapper;
    }

    @Override
    protected Game toBusinessObjectNoCheck(GameDTO transferObject) {
        Script script = scriptService.getScript(transferObject.scriptId());
        if (script == null) {
            throw new RelatedResourceNotFoundException(
                    "Script with id " + transferObject.scriptId() + " not found"
            );
        }

        List<PlayerParticipation> participations =
                Arrays.stream(transferObject.participants()).map(playerParticipationMapper::toBusinessObject).toList();

        if (transferObject.winningAlignment() != null) {
            return new Game(
                    transferObject.id(),
                    transferObject.version(),
                    participations,
                    script,
                    alignmentMapper.toBusinessObject(transferObject.winningAlignment()),
                    transferObject.description(),
                    transferObject.name()
            );
        }

        List<Player> winningPlayers = new ArrayList<>();
        for (Long playerId : transferObject.winningPlayerIds()) {
            Player player = playerService.getPlayerById(playerId);
            if (player == null) {
                throw new RelatedResourceNotFoundException("Player with id " + playerId + " not found");
            }
            winningPlayers.add(player);
        }
        return new Game(
                transferObject.id(),
                transferObject.version(),
                participations,
                script,
                transferObject.description(),
                winningPlayers,
                transferObject.name()
        );
    }

    @Override
    protected GameDTO toTransferObjectNoCheck(Game businessObject) {
        PlayerParticipationDTO[] participationDTOs = businessObject.getParticipants().stream()
                .map(playerParticipationMapper::toTransferObject).toArray(PlayerParticipationDTO[]::new);

        Long[] winningPlayerIds = businessObject.getWinningPlayers() == null ? null
                : businessObject.getWinningPlayers().stream().map(Player::getId).toArray(Long[]::new);

        return new GameDTO(
                businessObject.getId(),
                businessObject.getVersion(),
                businessObject.getName(),
                businessObject.getDescription(),
                businessObject.getScript().getId(),
                alignmentMapper.toTransferObject(businessObject.getWinningAlignment()),
                winningPlayerIds,
                participationDTOs
        );
    }
}
