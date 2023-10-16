package vnu.uet.moonbe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnu.uet.moonbe.dto.SongDTO;
import vnu.uet.moonbe.models.Song;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    @GetMapping("")
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDTO> getSongDetail(@PathVariable String id) {
        return ResponseEntity.ofNullable(null);
    }

    @PostMapping("/new")
    public void addSong(SongDTO song) {

    }

    @PutMapping("/{id}")
    public void updateSong(@PathVariable Long id, SongDTO song) {

    }

    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {

    }
}
