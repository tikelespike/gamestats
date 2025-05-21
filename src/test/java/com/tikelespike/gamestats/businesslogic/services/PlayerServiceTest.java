package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.GamestatsApplication;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.entities.UserCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GamestatsApplication.class)
class PlayerServiceTest {

    private static final long TEST_ID_1 = 12345L;
    private static final int TEST_ID_2 = 4242;
    private static final long TEST_ID_3 = 6969L;
    // CUT
    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;


    @Test
    void testCreateUnassignedPlayer() {
        Player createdPlayer = playerService.createPlayer("testplayer");

        assertNotNull(createdPlayer);
        assertEquals("testplayer", createdPlayer.getName());
    }

    @Test
    void testCreateAssignedPlayer() {
        User testUser = createTestUser("testCreateAssignedPlayer");

        Player createdPlayer = playerService.createPlayer(testUser);

        assertNotNull(createdPlayer);
        assertEquals(testUser.getId(), createdPlayer.getOwner().getId());
        assertEquals(testUser.getEmail(), createdPlayer.getOwner().getEmail());
        assertEquals(testUser.getName(), createdPlayer.getName());
    }

    @Test
    void testCreateUnassignedNullName() {
        assertThrows(NullPointerException.class, () -> playerService.createPlayer((String) null));
    }

    @Test
    void testCreateUnassignedEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> playerService.createPlayer("   "));
    }

    @Test
    void testCreateAssignedNullUser() {
        assertThrows(NullPointerException.class, () -> playerService.createPlayer((User) null));
    }

    @Test
    void testCreateAssignedUserAlreadyHasPlayer() {
        User testUser = createTestUser("testCreateAssignedUserAlreadyHasPlayer");

        playerService.createPlayer(testUser);

        assertThrows(IllegalStateException.class, () -> playerService.createPlayer(testUser));
    }

    @Test
    void testCreateAssignedInvalidUser() {
        User user = new User(TEST_ID_1, 0L, "I dont exist", "invalid@test.com", "password", null,
                UserRole.defaultRole());

        assertThrows(IllegalArgumentException.class, () -> playerService.createPlayer(user));
    }

    @Test
    void testGetAll() {
        User testUser = createTestUser("testGetAll");
        Player firstPlayer = playerService.createPlayer("Example Name");
        Player secondPlayer = playerService.createPlayer(testUser);


        List<Player> allPlayers = playerService.getAllPlayers();

        assertTrue(allPlayers.contains(firstPlayer));
        assertTrue(allPlayers.contains(secondPlayer));
    }

    @Test
    void testGetSingle() {
        Player player = playerService.createPlayer("testGetSingle");

        Player retrievedPlayer = playerService.getPlayerById(player.getId());

        assertEquals(player, retrievedPlayer);
    }

    @Test
    void testGetSingleNotExistent() {
        Player player = playerService.getPlayerById(TEST_ID_2);

        assertNull(player);
    }

    @Test
    void testUpdateOwner() {
        User testUser = createTestUser("testUpdateOwner");
        Player player = playerService.createPlayer("Old Name");
        player.setOwner(testUser);

        playerService.updatePlayer(player);

        assertEquals(player, playerService.getPlayerById(player.getId()));
    }

    @Test
    void testUpdateNonExistentPlayer() {
        Player player = new Player(TEST_ID_3, null, "testUpdateNonExistentPlayer", null);

        assertThrows(IllegalArgumentException.class, () -> playerService.updatePlayer(player));
    }

    @Test
    void testUpdateNullId() {
        Player player = new Player(null, null, "testUpdateNullId", null);
        assertThrows(IllegalArgumentException.class, () -> playerService.updatePlayer(player));
    }

    @Test
    void testUpdateNullPlayer() {
        assertThrows(NullPointerException.class, () -> playerService.updatePlayer(null));
    }

    @Test
    void testDeletePlayer() {
        Player player = playerService.createPlayer("testDeletePlayer");

        playerService.deletePlayer(player.getId());

        assertNull(playerService.getPlayerById(player.getId()));
    }

    private User createTestUser(String testId) {
        return userService.createUser(new UserCreationRequest("testuser_" + testId, "testuser_" + testId + "@test.de",
                "testpassword", UserRole.USER));
    }
}
