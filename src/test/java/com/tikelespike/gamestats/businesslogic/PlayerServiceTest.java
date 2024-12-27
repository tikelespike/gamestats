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
        User testUser = userService.signUp(new SignupRequest("testuser", "testuser", "testpassword"));

        Player createdPlayer = playerService.createPlayer(testUser);
        assertNotNull(createdPlayer);
        assertEquals(testUser.getId(), createdPlayer.getOwner().getId());
        assertEquals(testUser.getEmail(), createdPlayer.getOwner().getEmail());
        assertEquals(testUser.getName(), createdPlayer.getName());
    }
}
