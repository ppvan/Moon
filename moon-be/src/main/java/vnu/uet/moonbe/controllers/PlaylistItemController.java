package vnu.uet.moonbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnu.uet.moonbe.dto.DetailSongDto;
import vnu.uet.moonbe.services.PlaylistItemService;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
public class PlaylistItemController {

	private final PlaylistItemService playlistItemService;

	@GetMapping("/{id}")
	public List<DetailSongDto> getAllItems(@PathVariable int id) {
		return playlistItemService.getAllItems(id);
	}

	@PostMapping("/{playlistId}/song/{songId}")
	public ResponseEntity<?> addItem(
			@PathVariable int playlistId,
			@PathVariable int songId
	) {
		return playlistItemService.addItem(playlistId, songId);
	}

	@DeleteMapping("/{playlistId}/song/{songId}")
	public ResponseEntity<?> deleteItem(
			@PathVariable int playlistId,
			@PathVariable int songId
	) {
		return playlistItemService.deleteItem(playlistId, songId);
	}
}
