package com.tikelespike.gamestats.gateways.scripttool;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Reverse-engineered format of an official character (also known as role) as used in the official script tool.
 *
 * @param id string identifier
 * @param name display name
 * @param team character type (e.g. townsfolk)
 * @param edition which set the character was released as part of
 * @param firstNightReminder first night storyteller reminder text
 * @param otherNightReminder other nights storyteller reminder text
 * @param reminders reminder tokens belonging to this character
 * @param setup whether the character requires special setup before characters are assigned to the players
 * @param ability description of the character ability
 * @param flavor flavor text for the character
 */
@JsonIgnoreProperties(ignoreUnknown = true)
record OfficialCharacterDTO(
        String id,
        String name,
        String team,
        String edition,
        String firstNightReminder,
        String otherNightReminder,
        String[] reminders,
        boolean setup,
        String ability,
        String flavor
) {

}
