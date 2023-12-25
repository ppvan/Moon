package vnu.uet.moonbe.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vnu.uet.moonbe.dto.DetailSongDto;
import vnu.uet.moonbe.dto.ResponseDto;
import vnu.uet.moonbe.exceptions.PlaylistItemNotFoundException;
import vnu.uet.moonbe.exceptions.PlaylistNotFoundException;
import vnu.uet.moonbe.exceptions.SongNotFoundException;
import vnu.uet.moonbe.models.Playlist;
import vnu.uet.moonbe.models.PlaylistItem;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.repositories.PlaylistItemRepository;
import vnu.uet.moonbe.repositories.PlaylistRepository;
import vnu.uet.moonbe.repositories.SongRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistItemService {

	private final PlaylistRepository playlistRepository;
	private final PlaylistItemRepository playlistItemRepository;
	private final SongService songService;
	private final SongRepository songRepository;

	public List<DetailSongDto> getAllItems(int id) {
		Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
		if (optionalPlaylist.isEmpty()) {
			throw new PlaylistNotFoundException("Playlist not found");
		}

		Playlist playlist = optionalPlaylist.get();
		List<PlaylistItem> playlistItems = playlistItemRepository.findByPlaylist(playlist);

		return playlistItems.stream()
				.map(playlistItem -> songService.getSongById(playlistItem.getSong().getId()))
				.collect(Collectors.toList());
	}

	public ResponseEntity<?> addItem(int playlistId, int songId) {
		try {
			Playlist playlist = playlistRepository.findById(playlistId)
					.orElseThrow(() -> new PlaylistNotFoundException("Playlist could not found"));

			Song song = songRepository.findById(songId)
					.orElseThrow(() -> new SongNotFoundException("Song could not found"));

			PlaylistItem playlistItem = new PlaylistItem();
			playlistItem.setPlaylist(playlist);
			playlistItem.setSong(song);

			playlistItemRepository.save(playlistItem);

			ResponseDto responseDto = new ResponseDto();
			responseDto.setStatusCode(HttpStatus.OK.value());
			responseDto.setMessage("Add song to playlist successfully");

			return ResponseEntity.ok(responseDto);
		} catch (Exception e) {
			ResponseDto responseDto = new ResponseDto();
			responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseDto.setMessage("Song could not add to playlist");

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
		}
	}

	public ResponseEntity<?> deleteItem(int playlistId, int songId) {
		try {
			Playlist playlist = playlistRepository.findById(playlistId)
					.orElseThrow(() -> new PlaylistNotFoundException("Playlist could not found"));

			Song song = songRepository.findById(songId)
					.orElseThrow(() -> new SongNotFoundException("Song could not found"));

			PlaylistItem playlistItem = playlistItemRepository.findByPlaylistAndSong(playlist, song);
			if (playlistItem == null) {
				throw new PlaylistItemNotFoundException("No item found");
			}

			playlistItemRepository.delete(playlistItem);

			ResponseDto responseDto = new ResponseDto();
			responseDto.setStatusCode(HttpStatus.NO_CONTENT.value());
			responseDto.setMessage("Item deleted successfully");

			return ResponseEntity.ok(responseDto);
		} catch (Exception e) {
			ResponseDto responseDto = new ResponseDto();
			responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseDto.setMessage("Error deleting playlist");

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
		}
	}
}
