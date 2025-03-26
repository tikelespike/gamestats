package com.tikelespike.gamestats.gateways.scripttool;


/**
 * Reverse-engineered format of an official character (also known as role) as used in the official script tool.
 *
 * @param id string identifier
 * @param name display name
 * @param roleType character type (e.g. townsfolk)
 * @param print sub-url to image containing character description
 * @param icon sub-url to icon of character
 * @param version string identifier of the pack in which the character was released (e.g."1 -Trouble Brewing")
 * @param isDisabled unknown, likely whether the character is displayed in the script tool
 */
public record OfficialCharacterDTO(
        String id,
        String name,
        String roleType,
        String print,
        String icon,
        String version,
        boolean isDisabled
) {

}
