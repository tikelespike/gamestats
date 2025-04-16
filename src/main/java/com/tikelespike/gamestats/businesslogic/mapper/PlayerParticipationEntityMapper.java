package com.tikelespike.gamestats.businesslogic.mapper;

import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.common.Mapper;
import com.tikelespike.gamestats.data.entities.AlignmentEntity;
import com.tikelespike.gamestats.data.entities.CharacterEntity;
import com.tikelespike.gamestats.data.entities.PlayerParticipationEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between the player participation business object and the player participation entity database representation.
 */
@Component
public class PlayerParticipationEntityMapper extends Mapper<PlayerParticipation, PlayerParticipationEntity> {
    private final UserPlayerEntityMapper playerMapper;
    private final Mapper<Character, CharacterEntity> characterMapper;
    private final Mapper<Alignment, AlignmentEntity> alignmentMapper;

    /**
     * Creates a new mapper. This is usually done by the Spring framework, which manages the mapper's lifecycle and
     * injects the required dependencies.
     *
     * @param playerMapper mapper for player entities
     * @param characterMapper mapper for character entities
     * @param alignmentMapper mapper for alignment entities
     */
    public PlayerParticipationEntityMapper(UserPlayerEntityMapper playerMapper,
                                           Mapper<Character, CharacterEntity> characterMapper,
                                           Mapper<Alignment, AlignmentEntity> alignmentMapper) {
        this.playerMapper = playerMapper;
        this.characterMapper = characterMapper;
        this.alignmentMapper = alignmentMapper;
    }


    @Override
    protected PlayerParticipation toBusinessObjectNoCheck(PlayerParticipationEntity transferObject) {
        return new PlayerParticipation(
                playerMapper.toBusinessObject(transferObject.getPlayer()),
                characterMapper.toBusinessObject(transferObject.getInitialCharacter()),
                alignmentMapper.toBusinessObject(transferObject.getInitialAlignment()),
                characterMapper.toBusinessObject(transferObject.getEndCharacter()),
                alignmentMapper.toBusinessObject(transferObject.getEndAlignment()),
                transferObject.isAliveAtEnd()
        );
    }

    @Override
    protected PlayerParticipationEntity toTransferObjectNoCheck(PlayerParticipation businessObject) {
        return new PlayerParticipationEntity(
                null, // JPA will create completely new participation entries on every save, and remove the old ones
                // using orphanRemoval
                null,
                null, // needs to be set externally, for example by the game entity when the participation is added
                playerMapper.toTransferObject(businessObject.player()),
                characterMapper.toTransferObject(businessObject.initialCharacter()),
                alignmentMapper.toTransferObject(businessObject.initialAlignment()),
                characterMapper.toTransferObject(businessObject.endCharacter()),
                alignmentMapper.toTransferObject(businessObject.endAlignment()),
                businessObject.isAliveAtEnd()
        );
    }
}
