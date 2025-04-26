package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.AlignmentEntity;
import com.tikelespike.gamestats.data.entities.GameEntity;
import com.tikelespike.gamestats.data.entities.PlayerEntity;
import com.tikelespike.gamestats.data.entities.PlayerParticipationEntity;
import com.tikelespike.gamestats.data.entities.ScriptEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps between the game business object and the game entity database representation.
 */
@Component
public class GameEntityMapper extends Mapper<Game, GameEntity> {

    private final Mapper<Script, ScriptEntity> scriptMapper;
    private final Mapper<Alignment, AlignmentEntity> alignmentMapper;
    private final Mapper<PlayerParticipation, PlayerParticipationEntity> playerParticipationMapper;
    private final UserPlayerEntityMapper playerMapper;

    /**
     * Creates a new mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param scriptMapper mapper for script entities
     * @param alignmentMapper mapper for alignment entities
     * @param playerParticipationMapper mapper for player participation entities
     * @param playerMapper mapper for players
     */
    public GameEntityMapper(Mapper<Script, ScriptEntity> scriptMapper,
                            Mapper<Alignment, AlignmentEntity> alignmentMapper,
                            Mapper<PlayerParticipation, PlayerParticipationEntity> playerParticipationMapper,
                            UserPlayerEntityMapper playerMapper) {
        this.scriptMapper = scriptMapper;
        this.playerParticipationMapper = playerParticipationMapper;
        this.alignmentMapper = alignmentMapper;
        this.playerMapper = playerMapper;
    }

    @Override
    protected Game toBusinessObjectNoCheck(GameEntity transferObject) {
        Script script = scriptMapper.toBusinessObject(transferObject.getScript());
        List<PlayerParticipation> participations = transferObject.getParticipants().stream()
                .map(playerParticipationMapper::toBusinessObject)
                .toList();
        Alignment winningAlignment = alignmentMapper.toBusinessObject(transferObject.getWinningAlignment());
        List<Player> winningPlayers = transferObject.getWinningPlayers() == null ? null
                : transferObject.getWinningPlayers().stream()
                        .map(playerMapper::toBusinessObject)
                        .toList();
        List<Player> storytellers = transferObject.getStorytellers() == null ? new ArrayList<>()
                : transferObject.getStorytellers().stream()
                        .map(playerMapper::toBusinessObject)
                        .toList();

        if (winningAlignment != null) {
            return new Game(
                    transferObject.getId(),
                    transferObject.getVersion(),
                    participations,
                    script,
                    winningAlignment,
                    transferObject.getDescription(),
                    transferObject.getName(),
                    storytellers
            );
        }
        return new Game(
                transferObject.getId(),
                transferObject.getVersion(),
                participations,
                script,
                transferObject.getDescription(),
                winningPlayers,
                transferObject.getName(),
                storytellers
        );
    }

    @Override
    protected GameEntity toTransferObjectNoCheck(Game businessObject) {
        ScriptEntity script = scriptMapper.toTransferObject(businessObject.getScript());
        List<PlayerParticipationEntity> participations = businessObject.getParticipants().stream()
                .map(playerParticipationMapper::toTransferObject)
                .toList();
        AlignmentEntity winningAlignment = alignmentMapper.toTransferObject(businessObject.getWinningAlignment());
        List<PlayerEntity> winningPlayers = businessObject.getWinningPlayers() == null ? null
                : businessObject.getWinningPlayers().stream()
                        .map(playerMapper::toTransferObject)
                        .toList();
        List<PlayerEntity> storytellers = businessObject.getStorytellers() == null ? new ArrayList<>()
                : businessObject.getStorytellers().stream()
                        .map(playerMapper::toTransferObject)
                        .toList();

        return new GameEntity(
                businessObject.getId(),
                businessObject.getVersion(),
                script,
                winningAlignment,
                businessObject.getDescription(),
                participations,
                winningPlayers,
                businessObject.getName(),
                storytellers
        );
    }
}
