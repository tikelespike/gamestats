package com.tikelespike.gamestats.api.resources;

import com.tikelespike.gamestats.api.entities.PlayerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public final class PlayerController {

    @GetMapping()
    public ResponseEntity<List<PlayerDTO>> getPlayers() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping()
    public ResponseEntity<PlayerDTO> createPlayer(PlayerDTO player) {
        return ResponseEntity.internalServerError().build();
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
