package vnu.uet.moonbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnu.uet.moonbe.dto.SongDTO;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.services.SongService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/all")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongDetail(@PathVariable int id) {
        Song song = songService.getSongById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Song>> getSongsByTitle(@RequestParam String title) {
        List<Song> songs = songService.getSongsByTitle(title);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/search/artist")
    public ResponseEntity<List<Song>> getSongsByArtist(@RequestParam String artist) {
        List<Song> songs = songService.getSongsByArtist(artist);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/search/genre")
    public ResponseEntity<List<Song>> getSongsByGenre(@RequestParam String genre) {
        List<Song> songs = songService.getSongsByGenre(genre);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSong(@RequestBody SongDTO songDTO) {
        return songService.addSong(songDTO);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateSong(
        @PathVariable int id,
        @RequestBody SongDTO songDTO
    ) {
        return songService.updateSong(id, songDTO);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteSong(@PathVariable int id) {
        return songService.deleteSong(id);
    }
}
