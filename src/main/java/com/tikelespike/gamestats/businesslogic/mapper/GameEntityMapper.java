package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.Game;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.entities.Script;
import com.tikelespike.gamestats.businesslogic.entities.SimpleGame;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.AlignmentEntity;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
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
    private final UserPlayerEntityMapper playerMapper;
    private final Mapper<Character, CharacterEntity> characterMapper;
    private final Mapper<Alignment, AlignmentEntity> alignmentMapper;

    /**
     * Creates a new mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param scriptMapper mapper for script entities
     * @param playerMapper mapper for player entities
     * @param characterMapper mapper for character entities
     * @param alignmentMapper mapper for alignment entities
     */
    public GameEntityMapper(Mapper<Script, ScriptEntity> scriptMapper,
                            UserPlayerEntityMapper playerMapper,
                            Mapper<Character, CharacterEntity> characterMapper,
                            Mapper<Alignment, AlignmentEntity> alignmentMapper) {
        this.scriptMapper = scriptMapper;
        this.playerMapper = playerMapper;
        this.characterMapper = characterMapper;
        this.alignmentMapper = alignmentMapper;
    }

    @Override
    protected Game toBusinessObjectNoCheck(GameEntity transferObject) {
        List<PlayerParticipation> participations = transferObject.getParticipants().stream()
                .map(this::toBusinessObject)
                .collect(Collectors.toList());

        return new SimpleGame(
                transferObject.getId(),
                transferObject.getVersion(),
                participations,
                scriptMapper.toBusinessObject(transferObject.getScript()),
                alignmentMapper.toBusinessObject(transferObject.getWinningAlignment()),
                transferObject.getDescription()
        );
    }

    @Override
    protected GameEntity toTransferObjectNoCheck(Game businessObject) {
        List<PlayerParticipationEntity> participations = businessObject.getParticipants().stream()
                .map(this::toTransferObject)
                .collect(Collectors.toList());

        return new GameEntity(
                businessObject.getId(),
                businessObject.getVersion(),
                scriptMapper.toTransferObject(businessObject.getScript()),
                alignmentMapper.toTransferObject(businessObject.getWinningAlignment()),
                businessObject.getDescription(),
                participations
        );
    }

    private PlayerParticipation toBusinessObject(PlayerParticipationEntity transferObject) {
        return new PlayerParticipation(
                playerMapper.toBusinessObject(transferObject.getPlayer()),
                characterMapper.toBusinessObject(transferObject.getInitialCharacter()),
                alignmentMapper.toBusinessObject(transferObject.getInitialAlignment()),
                characterMapper.toBusinessObject(transferObject.getEndCharacter()),
                alignmentMapper.toBusinessObject(transferObject.getEndAlignment()),
                transferObject.isAliveAtEnd()
        );
    }

    private PlayerParticipationEntity toTransferObject(PlayerParticipation businessObject) {
        return new PlayerParticipationEntity(
                null, // JPA will create completely new participation entries on every save, and remove the old ones
                // using orphanRemoval
                null,
                null, // game will be set by JPA
                playerMapper.toTransferObject(businessObject.player()),
                characterMapper.toTransferObject(businessObject.initialCharacter()),
                alignmentMapper.toTransferObject(businessObject.initialAlignment()),
                characterMapper.toTransferObject(businessObject.endCharacter()),
                alignmentMapper.toTransferObject(businessObject.endAlignment()),
                businessObject.isAliveAtEnd()
        );
    }
}
