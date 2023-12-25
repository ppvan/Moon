package vnu.uet.moonbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnu.uet.moonbe.dto.PlaylistDto;
import vnu.uet.moonbe.models.Playlist;
import vnu.uet.moonbe.services.PlaylistService;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
public class PlaylistController {

	private final PlaylistService playlistService;

	@GetMapping("/all")
	public List<PlaylistDto> getAllPlaylists() {
		return playlistService.getAllPlaylists();
	}

	@GetMapping("/")
	public List<PlaylistDto> getPlaylists() {
		return playlistService.getPlaylists();
	}

	@PostMapping("/")
	public ResponseEntity<?> createPlaylist(
			@RequestBody PlaylistDto playlistDto
	) throws Exception {
		return playlistService.createPlaylist(playlistDto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> updatePlaylist(
			@PathVariable int id,
			@RequestBody PlaylistDto playlistDto
	) {
		return playlistService.updatePlaylist(id, playlistDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePlaylist(
			@PathVariable int id
	) {
		return playlistService.deletePlaylist(id);
	}
}