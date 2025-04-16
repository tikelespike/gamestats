package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.AlignmentDTO;
import com.tikelespike.gamestats.api.entities.PlayerParticipationDTO;
import com.tikelespike.gamestats.businesslogic.entities.Alignment;
import com.tikelespike.gamestats.businesslogic.entities.Character;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.PlayerParticipation;
import com.tikelespike.gamestats.businesslogic.exceptions.RelatedResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.services.CharacterService;
import com.tikelespike.gamestats.businesslogic.services.PlayerService;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

/**
 * Maps between player participation business objects and their REST transfer representation.
 */
@Component
public class PlayerParticipationMapper extends Mapper<PlayerParticipation, PlayerParticipationDTO> {

    private final Mapper<Alignment, AlignmentDTO> alignmentMapper;
    private final PlayerService playerService;
    private final CharacterService characterService;

    /**
     * Creates a new player participation mapper. This is usually done by the Spring framework, which manages the
     * mapper's lifecycle and injects the required dependencies.
     *
     * @param alignmentMapper mapper for alignments
     * @param playerService service to load players from ids
     * @param characterService service to load characters from ids
     */
    public PlayerParticipationMapper(AlignmentMapper alignmentMapper, PlayerService playerService,
                                     CharacterService characterService) {
        this.alignmentMapper = alignmentMapper;
        this.playerService = playerService;
        this.characterService = characterService;
    }

    @Override
    protected PlayerParticipation toBusinessObjectNoCheck(PlayerParticipationDTO transferObject) {
        Player player;
        try {
            player = playerService.getPlayerById(transferObject.playerId());
        } catch (ResourceNotFoundException e) {
            throw new RelatedResourceNotFoundException(e);
        }

        Character initialCharacter;
        try {
            initialCharacter = characterService.getCharacter(transferObject.initialCharacterId());
        } catch (ResourceNotFoundException e) {
            throw new RelatedResourceNotFoundException(e);
        }

        Character endCharacter;
        try {
            endCharacter = transferObject.endCharacterId() == null ? null
                    : characterService.getCharacter(transferObject.endCharacterId());
        } catch (ResourceNotFoundException e) {
            throw new RelatedResourceNotFoundException(e);
        }

        return new PlayerParticipation(
                player,
                initialCharacter,
                alignmentMapper.toBusinessObject(transferObject.initialAlignment()),
                endCharacter,
                alignmentMapper.toBusinessObject(transferObject.endAlignment()),
                transferObject.isAliveAtEnd()
        );
    }

    @Override
    protected PlayerParticipationDTO toTransferObjectNoCheck(PlayerParticipation businessObject) {
        return new PlayerParticipationDTO(
                businessObject.player().getId(),
                businessObject.initialCharacter().getId(),
                alignmentMapper.toTransferObject(businessObject.initialAlignment()),
                businessObject.endCharacter().getId(),
                alignmentMapper.toTransferObject(businessObject.endAlignment()),
                businessObject.isAliveAtEnd()
        );
    }
}
