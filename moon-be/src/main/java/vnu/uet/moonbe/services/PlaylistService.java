package vnu.uet.moonbe.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vnu.uet.moonbe.dto.PlaylistDto;
import vnu.uet.moonbe.dto.ResponseDto;
import vnu.uet.moonbe.exceptions.PlaylistNotFoundException;
import vnu.uet.moonbe.models.Playlist;
import vnu.uet.moonbe.models.PlaylistItem;
import vnu.uet.moonbe.models.User;
import vnu.uet.moonbe.repositories.PlaylistRepository;
import vnu.uet.moonbe.repositories.PlaylistItemRepository;
import vnu.uet.moonbe.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

	private final UserRepository userRepository;
	private final PlaylistRepository playlistRepository;
	private final PlaylistItemRepository playlistItemRepository;

	public List<PlaylistDto> getAllPlaylists() {
		List<Playlist> playlists = playlistRepository.findAll();

		return playlists.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public List<PlaylistDto> getPlaylists() {
		User user = getAuthenticatedUser();

		List<Playlist> playlists = playlistRepository.findByUser(user);

		return playlists.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public ResponseEntity<?> createPlaylist(PlaylistDto playlistDto) throws Exception {
		User user = getAuthenticatedUser();

		if (playlistDto == null || playlistDto.getName() == null || playlistDto.getName().trim().isEmpty()) {
			throw new PlaylistNotFoundException("No playlist details found");
		}

		Playlist playlist = new Playlist();
		playlist.setName(playlistDto.getName());
		playlist.setUser(user);

		playlistRepository.save(playlist);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatusCode(HttpStatus.CREATED.value());
		responseDto.setMessage("Playlist created success");

		return ResponseEntity.ok(responseDto);
	}

	public ResponseEntity<?> updatePlaylist(int id, PlaylistDto playlistDto) {
		Playlist playlist = playlistRepository.findById(id)
				.orElseThrow(() -> new PlaylistNotFoundException("Playlist could not be updated"));

		playlist.setName(playlistDto.getName());

		playlistRepository.save(playlist);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatusCode(HttpStatus.OK.value());
		responseDto.setMessage("Playlist updated success");

		return ResponseEntity.ok(responseDto);
	}

	@Transactional
	public ResponseEntity<?> deletePlaylist(int id) {
		try {
			Playlist playlist = playlistRepository.findById(id)
					.orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));

			List<PlaylistItem> playlistItems = playlist.getPlaylistItems();
			playlistItemRepository.deleteAll(playlistItems);

			playlistRepository.delete(playlist);

			ResponseDto responseDto = new ResponseDto();
			responseDto.setStatusCode(HttpStatus.NO_CONTENT.value());
			responseDto.setMessage("Playlist deleted successfully");

			return ResponseEntity.ok(responseDto);
		} catch (Exception e) {
			ResponseDto responseDto = new ResponseDto();
			responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseDto.setMessage("Error deleting playlist");

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
		}
	}

	private User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("User not authenticated");
		}

		String userEmail = authentication.getName();

		return userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	private PlaylistDto mapToDto(Playlist playlist) {
		PlaylistDto playlistDto = new PlaylistDto();

		playlistDto.setId(playlist.getId());
		playlistDto.setName(playlist.getName());
		playlistDto.setUserName(playlist.getUser().getFirstname() + playlist.getUser().getLastname());

		return playlistDto;
	}
}