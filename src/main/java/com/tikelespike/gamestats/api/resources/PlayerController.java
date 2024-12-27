package com.tikelespike.gamestats.api.resources;

import com.tikelespike.gamestats.api.entities.PlayerDTO;
import com.tikelespike.gamestats.api.mapper.PlayerMapper;
import com.tikelespike.gamestats.businesslogic.PlayerService;
import com.tikelespike.gamestats.businesslogic.UserService;
import com.tikelespike.gamestats.businesslogic.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public final class PlayerController {

    private final PlayerService playerService;
    private final UserService userService;
    private final PlayerMapper playerMapper;

    public PlayerController(PlayerService playerService, UserService userService, PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.userService = userService;
        this.playerMapper = playerMapper;
    }

    @GetMapping()
    public ResponseEntity<List<PlayerDTO>> getPlayers() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping()
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO player) {
        PlayerDTO createdPlayer;
        if (player.getOwnerId() == null) {
            createdPlayer = playerMapper.toTransferObject(playerService.createPlayer(player.getName()));
        } else {
            User owner = userService.loadUser(player.getOwnerId());
            createdPlayer = playerMapper.toTransferObject(playerService.createPlayer(owner));
        }
        return ResponseEntity.created(URI.create("/api/v1/players/" + createdPlayer.getId())).body(createdPlayer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable("id") long id) {
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable("id") long id, PlayerDTO player) {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable("id") long id) {
        return ResponseEntity.notFound().build();
    }
}
