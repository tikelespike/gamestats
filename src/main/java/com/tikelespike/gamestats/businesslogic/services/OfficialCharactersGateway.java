package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.ExternalServiceUnavailableException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Gateway to official BOTC tools for retrieving official, published characters.
 */
@Service
public interface OfficialCharactersGateway {

    /**
     * Retrieves all characters that are officially published and that can be imported into the application.
     *
     * @return a list of all officially published characters (except those that are not supported by the application) as
     *         creation requests to import them into this application
     * @throws ExternalServiceUnavailableException if the official tools the characters are retrieved from is
     *         not available
     */
    List<CharacterCreationRequest> getAllOfficialCharacters() throws ExternalServiceUnavailableException;

}
