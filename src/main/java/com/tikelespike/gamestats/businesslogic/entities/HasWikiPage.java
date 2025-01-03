package com.tikelespike.gamestats.businesslogic.entities;

/**
 * A game concept that has a page on an internet wiki associated with it, providing more detailed information.
 */
public interface HasWikiPage {
    /**
     * Returns the URL of the wiki page associated with this game concept.
     * <p>
     * Example: {@code https://wiki.bloodontheclocktower.com/Fortune_Teller}
     *
     * @return the URL of the wiki page associated with this game concept
     */
    String getWikiPageLink();
}
