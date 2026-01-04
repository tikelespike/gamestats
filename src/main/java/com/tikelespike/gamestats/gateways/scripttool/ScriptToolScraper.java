package com.tikelespike.gamestats.gateways.scripttool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikelespike.gamestats.businesslogic.entities.CharacterCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.CharacterType;
import com.tikelespike.gamestats.businesslogic.exceptions.ExternalServiceUnavailableException;
import com.tikelespike.gamestats.businesslogic.services.OfficialCharactersGateway;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool to retrieve the official characters by scraping the official script tool.
 */
@Service
public class ScriptToolScraper implements OfficialCharactersGateway {

    private static final String SCRIPT_TOOL_URL = "https://script.bloodontheclocktower.com";
    private static final String WIKI = "https://wiki.bloodontheclocktower.com/";
    private static final String ICON_BASE_URL = SCRIPT_TOOL_URL + "/src/assets/icons";
    private static final Map<String, CharacterType> ROLE_TYPE_MAP = Map.of(
            "townsfolk", CharacterType.TOWNSFOLK,
            "outsider", CharacterType.OUTSIDER,
            "minion", CharacterType.MINION,
            "demon", CharacterType.DEMON,
            "travellers", CharacterType.TRAVELLER
    );
    private static final Map<CharacterType, String> TYPE_ICON_SUFFIX_MAP = Map.of(
            CharacterType.TOWNSFOLK, "_g",
            CharacterType.OUTSIDER, "_g",
            CharacterType.MINION, "_e",
            CharacterType.DEMON, "_e",
            CharacterType.TRAVELLER, ""
    );

    /**
     * Creates a new gateway for accessing the official script tool.
     */
    public ScriptToolScraper() {
    }

    @Override
    public List<CharacterCreationRequest> getAllOfficialCharacters() throws ExternalServiceUnavailableException {
        String rolesJson = fetchRolesFromScriptTool();
        ObjectMapper mapper = new ObjectMapper();
        List<OfficialCharacterDTO> characters;
        try {
            characters = mapper.readValue(rolesJson,
                    new TypeReference<>() {
                    });
        } catch (JsonProcessingException e) {
            throw new ExternalServiceUnavailableException("Error parsing characters from official script tool.", e);
        }
        return characters.stream()
                .filter(r -> ROLE_TYPE_MAP.containsKey(r.team())) // Filter out unsupported role types
                .map(this::mapToCreationSuggestion).toList();
    }

    private String fetchRolesFromScriptTool() throws ExternalServiceUnavailableException {
        // At the time of writing, the JSON object containing the character information is hidden somewhere in a
        // bundled and minified JS file of the script tool. The following code attempts to find and extract it.

        Document document;
        try {
            document = Jsoup.connect(SCRIPT_TOOL_URL).get();
        } catch (IOException e) {
            throw new ExternalServiceUnavailableException("Error connecting to official script tool", e);
        }
        List<String> scriptUrls = document.select("script[src]")
                .stream()
                .map(s -> s.attr("abs:src"))
                .filter(src -> src.contains("workspace.") && src.endsWith(".js"))
                .toList();
        if (scriptUrls.isEmpty()) {
            throw new ExternalServiceUnavailableException("Error retrieving characters from official script tool."
                    + " Could not find the source script of the BOTC script tool.");
        }
        String rolesJson;
        try {
            rolesJson = findRolesJson(scriptUrls);
        } catch (IOException e) {
            throw new ExternalServiceUnavailableException("Error retrieving characters from official script tool. "
                    + "Could not extract the characters.", e);
        }
        if (rolesJson == null) {
            throw new ExternalServiceUnavailableException("Could not find roles JSON object.");
        }
        return rolesJson;
    }

    private String findRolesJson(List<String> scripts) throws IOException {
        for (String scriptUrl : scripts) {
            String js = fetchJsonFromUrl(scriptUrl);

            // Reverse engineered pattern to find the relevant JSON object in the JS file - expected to break at some
            // point in the future
            Pattern parsePattern = Pattern.compile("JSON\\.parse\\('(.*?)'\\)", Pattern.DOTALL);
            Matcher matcher = parsePattern.matcher(js);

            while (matcher.find()) {
                String candidate = matcher.group(1);
                if (candidate.contains("\"id\":\"steward\"")) {
                    return StringEscapeUtils.unescapeEcmaScript(matcher.group(1));
                }
            }
        }
        return null;
    }

    private static @NonNull String fetchJsonFromUrl(String scriptUrl) throws IOException {
        return Jsoup.connect(scriptUrl)
                .ignoreContentType(true)
                .execute()
                .body();
    }

    private CharacterCreationRequest mapToCreationSuggestion(OfficialCharacterDTO dto) {
        if (!ROLE_TYPE_MAP.containsKey(dto.team())) {
            throw new NotImplementedException("Unknown character type: " + dto.team());
        }

        CharacterType characterType = ROLE_TYPE_MAP.get(dto.team());
        return new CharacterCreationRequest(
                dto.id(),
                dto.name(),
                characterType,
                WIKI + dto.name().replace(" ", "_"),
                ICON_BASE_URL + "/" + dto.edition() + "/" + dto.id() + TYPE_ICON_SUFFIX_MAP.get(characterType) + ".webp"
        );
    }
}
