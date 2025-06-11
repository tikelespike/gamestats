package com.tikelespike.gamestats.api.mapper;

import com.tikelespike.gamestats.api.entities.CharacterTypeDTO;
import com.tikelespike.gamestats.api.entities.PlayerStatsDTO;
import com.tikelespike.gamestats.businesslogic.entities.PlayerStats;
import com.tikelespike.gamestats.common.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps between the player statistics business object and its transfer object representation.
 */
@Component
public class PlayerStatsMapper extends Mapper<PlayerStats, PlayerStatsDTO> {
    private final CharacterTypeMapper characterTypeMapper;

    /**
     * Creates a new player statistics mapper. This is usually done by the Spring framework, which manages the mapper's
     * lifecycle and injects the required dependencies.
     *
     * @param characterTypeMapper mapper for character types, used to convert character type counts in player
     *         statistics
     */
    public PlayerStatsMapper(CharacterTypeMapper characterTypeMapper) {
        super();
        this.characterTypeMapper = characterTypeMapper;
    }

    @Override
    protected PlayerStats toBusinessObjectNoCheck(PlayerStatsDTO transferObject) {
        throw new UnsupportedOperationException("Currently not implemented");
    }

    @Override
    protected PlayerStatsDTO toTransferObjectNoCheck(PlayerStats businessObject) {
        Map<CharacterTypeDTO, Integer> convertedTypeCounts = new HashMap<>();
        businessObject.characterTypeCounts().forEach((type, count) -> {
            CharacterTypeDTO typeDTO = characterTypeMapper.toTransferObjectNoCheck(type);
            convertedTypeCounts.put(typeDTO, count);
        });

        Map<Long, Integer> convertedCharacterCounts = new HashMap<>();
        businessObject.characterPlayingCounts().forEach((character, count) ->
                convertedCharacterCounts.put(character.getId(), count)
        );

        return new PlayerStatsDTO(
                businessObject.player().getId(),
                businessObject.totalGamesPlayed(),
                businessObject.totalWins(),
                businessObject.timesStoryteller(),
                businessObject.timesDeadAtEnd(),
                businessObject.timesGood(),
                businessObject.timesEvil(),
                convertedTypeCounts,
                convertedCharacterCounts
        );
    }
}
