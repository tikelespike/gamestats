package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.AlignmentDTO;
import com.tikelespike.gamestats.api.entities.GameCreationDTO;
import com.tikelespike.gamestats.api.entities.PlayerParticipationDTO;
import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.GameCreationRequest;
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
 * Maps between game creation request business objects and their REST transfer representation.
 */
@Component
public class GameCreationRequestMapper extends Mapper<GameCreationRequest, GameCreationDTO> {

    private final ScriptService scriptService;
    private final PlayerService playerService;
    private final Mapper<Alignment, AlignmentDTO> alignmentMapper;
    private final Mapper<PlayerParticipation, PlayerParticipationDTO> playerParticipationMapper;

    /**
     * Creates a new game creation request mapper. This is usually done by the Spring framework, which manages the
     * mapper's lifecycle and injects the required dependencies.
     *
     * @param scriptService service to load scripts from ids
     * @param playerService service to load players from ids
     * @param alignmentMapper mapper for alignments
     * @param playerParticipationMapper mapper for player participations
     */
    public GameCreationRequestMapper(ScriptService scriptService, PlayerService playerService,
                                     AlignmentMapper alignmentMapper,
                                     PlayerParticipationMapper playerParticipationMapper) {
        this.scriptService = scriptService;
        this.playerService = playerService;
        this.alignmentMapper = alignmentMapper;
        this.playerParticipationMapper = playerParticipationMapper;
    }

    @Override
    protected GameCreationRequest toBusinessObjectNoCheck(GameCreationDTO transferObject) {
        Script script = scriptService.getScript(transferObject.scriptId());

        if (script == null) {
            throw new RelatedResourceNotFoundException(
                    "Script with id " + transferObject.scriptId() + " not found"
            );
        }

        List<PlayerParticipation> participations =
                Arrays.stream(transferObject.participants()).map(playerParticipationMapper::toBusinessObject).toList();

        List<Player> storytellers = new ArrayList<>();
        if (transferObject.storytellerIds() != null) {
            for (Long playerId : transferObject.storytellerIds()) {
                Player player = playerService.getPlayerById(playerId);
                if (player == null) {
                    throw new RelatedResourceNotFoundException("Player with id " + playerId + " not found");
                }
                storytellers.add(player);
            }
        }

        if (transferObject.winningAlignment() != null) {
            return new GameCreationRequest(
                    script,
                    participations,
                    alignmentMapper.toBusinessObject(transferObject.winningAlignment()),
                    transferObject.description(),
                    null,
                    transferObject.name(),
                    storytellers
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
        return new GameCreationRequest(
                script,
                participations,
                null,
                transferObject.description(),
                winningPlayers,
                transferObject.name(),
                storytellers
        );
    }

    @Override
    protected GameCreationDTO toTransferObjectNoCheck(GameCreationRequest businessObject) {
        PlayerParticipationDTO[] participationDTOs = businessObject.participants().stream()
                .map(playerParticipationMapper::toTransferObject).toArray(PlayerParticipationDTO[]::new);

        Long[] winningPlayerIds = businessObject.winningPlayers() == null ? null
                : businessObject.winningPlayers().stream().map(Player::getId).toArray(Long[]::new);

        Long[] storytellerIds = businessObject.storytellers() == null ? null
                : businessObject.storytellers().stream().map(Player::getId).toArray(Long[]::new);


        return new GameCreationDTO(
                businessObject.name(),
                businessObject.description(),
                businessObject.script().getId(),
                storytellerIds,
                alignmentMapper.toTransferObject(businessObject.winningAlignment()),
                winningPlayerIds,
                participationDTOs
        );
    }
}
