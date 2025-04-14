package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.AlignmentEntity;
import com.tikelespike.gamestats.data.entities.GameEntity;
import com.tikelespike.gamestats.data.entities.PlayerParticipationEntity;
import com.tikelespike.gamestats.data.entities.ScriptEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        List<PlayerParticipation> participations = transferObject.getParticipants().stream()
                .map(playerParticipationMapper::toBusinessObject)
                .collect(Collectors.toList());

        if (transferObject.getWinningAlignment() != null) {
            return new Game(
                    transferObject.getId(),
                    transferObject.getVersion(),
                    participations,
                    scriptMapper.toBusinessObject(transferObject.getScript()),
                    alignmentMapper.toBusinessObject(transferObject.getWinningAlignment()),
                    transferObject.getDescription()
            );
        }
        return new Game(
                transferObject.getId(),
                transferObject.getVersion(),
                participations,
                scriptMapper.toBusinessObject(transferObject.getScript()),
                transferObject.getDescription(),
                transferObject.getWinningPlayers().stream().map(playerMapper::toBusinessObject).toList()
        );

    }

    @Override
    protected GameEntity toTransferObjectNoCheck(Game businessObject) {
        List<PlayerParticipationEntity> participations = businessObject.getParticipants().stream()
                .map(playerParticipationMapper::toTransferObject)
                .collect(Collectors.toList());

        if (businessObject.getWinningAlignment() != null) {
            return new GameEntity(
                    businessObject.getId(),
                    businessObject.getVersion(),
                    scriptMapper.toTransferObject(businessObject.getScript()),
                    alignmentMapper.toTransferObject(businessObject.getWinningAlignment()),
                    businessObject.getDescription(),
                    participations,
                    null
            );
        }
        return new GameEntity(
                businessObject.getId(),
                businessObject.getVersion(),
                scriptMapper.toTransferObject(businessObject.getScript()),
                null,
                businessObject.getDescription(),
                participations,
                businessObject.getWinningPlayers().stream().map(playerMapper::toTransferObject).toList()
        );
    }
}
