package vnu.uet.moonbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnu.uet.moonbe.models.Playlist;
import vnu.uet.moonbe.repositories.PlaylistRepository;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private final PlaylistRepository repository;

    public PlaylistController(@Autowired PlaylistRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Long id) {
        var playlist = repository.findById(id);
        return playlist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<Playlist> addPlaylist(@RequestBody Playlist playlist) {
        return ResponseEntity.ok(playlist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long id) {
        return ResponseEntity.ok("Playlist has been deleted");
    }

}
