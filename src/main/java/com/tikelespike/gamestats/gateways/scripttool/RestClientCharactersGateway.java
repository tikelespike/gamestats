package com.tikelespike.gamestats.gateways.scripttool;

import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.exceptions.ExternalServiceUnavailableException;
import com.tikelespike.gamestats.businesslogic.services.OfficialCharactersGateway;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Gateway to the official script tool for retrieving characters implemented via a Spring REST client.
 */
@Service
public class RestClientCharactersGateway implements OfficialCharactersGateway {

    private static final String SCRIPT_TOOL_URL = "https://script.bloodontheclocktower.com";
    private static final String CHARACTERS_ENDPOINT = "/data/roles.json";
    private static final String WIKI = "https://wiki.bloodontheclocktower.com/";
    private static final Map<String, CharacterType> ROLE_TYPE_MAP = Map.of(
            "townsfolk", CharacterType.TOWNSFOLK,
            "outsider", CharacterType.OUTSIDER,
            "minion", CharacterType.MINION,
            "demon", CharacterType.DEMON,
            "travellers", CharacterType.TRAVELLER
    );

    private final RestClient client;

    /**
     * Creates a new gateway for accessing the official script tool via a Spring RestClient.
     */
    public RestClientCharactersGateway() {
        client = RestClient.builder()
                .baseUrl(SCRIPT_TOOL_URL)
                .build();
    }

    @Override
    public List<CharacterCreationRequest> getAllOfficialCharacters() throws ExternalServiceUnavailableException {
        ResponseEntity<OfficialCharacterDTO[]> response;
        try {
            response = client.get().uri(CHARACTERS_ENDPOINT).retrieve().toEntity(OfficialCharacterDTO[].class);
        } catch (RestClientResponseException e) {
            throw new ExternalServiceUnavailableException("Error retrieving characters from official script tool", e);
        }
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ExternalServiceUnavailableException("Error retrieving characters from official script tool");
        }

        return Arrays.stream(response.getBody()).filter(r -> ROLE_TYPE_MAP.containsKey(r.roleType()))
                .map(this::mapToCreationSuggestion).toList();
    }

    private CharacterCreationRequest mapToCreationSuggestion(OfficialCharacterDTO dto) {
        if (!ROLE_TYPE_MAP.containsKey(dto.roleType())) {
            throw new NotImplementedException("Unknown character type: " + dto.roleType());
        }

        return new CharacterCreationRequest(
                dto.id(),
                dto.name(),
                ROLE_TYPE_MAP.get(dto.roleType()),
                WIKI + dto.name().replace(" ", "_"), // In the future, we may want to validate if the page exists, first
                SCRIPT_TOOL_URL + dto.icon().substring(1) // Remove the leading dot
        );
    }
}
