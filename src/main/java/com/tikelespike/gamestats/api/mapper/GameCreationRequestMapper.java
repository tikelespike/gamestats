package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.AlignmentDTO;
import com.tikelespike.gamestats.api.entities.GameCreationRequestDTO;
import com.tikelespike.gamestats.api.entities.PlayerParticipationDTO;
import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.GameCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
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
public class GameCreationRequestMapper extends Mapper<GameCreationRequest, GameCreationRequestDTO> {

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
    protected GameCreationRequest toBusinessObjectNoCheck(GameCreationRequestDTO transferObject) {
        Script script;
        try {
            script = scriptService.getScript(transferObject.scriptId());
        } catch (ResourceNotFoundException e) {
            throw new RelatedResourceNotFoundException(e);
        }

        List<PlayerParticipation> participations =
                Arrays.stream(transferObject.participants()).map(playerParticipationMapper::toBusinessObject).toList();

        if (transferObject.winningAlignment() != null) {
            return new GameCreationRequest(
                    script,
                    participations,
                    alignmentMapper.toBusinessObject(transferObject.winningAlignment()),
                    transferObject.description(),
                    null
            );
        }

        List<Player> winningPlayers = new ArrayList<>();
        for (Long playerId : transferObject.winningPlayerIds()) {
            try {
                winningPlayers.add(playerService.getPlayerById(playerId));
            } catch (ResourceNotFoundException e) {
                throw new RelatedResourceNotFoundException(e);
            }
        }
        return new GameCreationRequest(
                script,
                participations,
                null,
                transferObject.description(),
                winningPlayers
        );
    }

    @Override
    protected GameCreationRequestDTO toTransferObjectNoCheck(GameCreationRequest businessObject) {
        PlayerParticipationDTO[] participationDTOs = businessObject.participants().stream()
                .map(playerParticipationMapper::toTransferObject).toArray(PlayerParticipationDTO[]::new);

        Long[] winningPlayerIds = businessObject.winningPlayers() == null ? null
                : businessObject.winningPlayers().stream().map(Player::getId).toArray(Long[]::new);

        return new GameCreationRequestDTO(
                businessObject.script().getId(),
                businessObject.description(),
                alignmentMapper.toTransferObject(businessObject.winningAlignment()),
                winningPlayerIds,
                participationDTOs
        );
    }
}
