package com.tikelespike.gamestats.businesslogic;

import com.tikelespike.gamestats.GamestatsApplication;
import com.tikelespike.gamestats.businesslogic.entities.Player;
import com.tikelespike.gamestats.businesslogic.entities.SignupRequest;
import com.tikelespike.gamestats.businesslogic.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = GamestatsApplication.class)
class PlayerServiceTest {

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

    private User createTestUser(String testId) {
        return userService.signUp(new SignupRequest("testuser_" + testId, "testuser_" + testId + "@test.de",
                "testpassword"));
    }
}
