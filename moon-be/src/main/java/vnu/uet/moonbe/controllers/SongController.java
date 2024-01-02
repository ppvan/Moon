package vnu.uet.moonbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vnu.uet.moonbe.dto.DetailSongDto;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.repositories.SongRepository;
import vnu.uet.moonbe.services.SongService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

	private final SongService songService;

	private final SongRepository songRepository;

	@GetMapping("/")
	public ResponseEntity<List<DetailSongDto>> getAllSongs() {
		List<DetailSongDto> songs = songService.getAllSongs();
		return new ResponseEntity<>(songs, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetailSongDto> getSongDetail(@PathVariable int id) {
		DetailSongDto detailSongDTO = songService.getSongById(id);
		return new ResponseEntity<>(detailSongDTO, HttpStatus.OK);
	}

	@GetMapping("/title/{title}")
	public ResponseEntity<List<DetailSongDto>> getSongsByTitle(@PathVariable String title) {
		List<DetailSongDto> songs = songService.getSongsByTitle(title);
		return new ResponseEntity<>(songs, HttpStatus.OK);
	}

	@GetMapping("/artist/{artist}")
	public ResponseEntity<List<DetailSongDto>> getSongsByArtist(@PathVariable String artist) {
		List<DetailSongDto> songs = songService.getSongsByArtist(artist);
		return new ResponseEntity<>(songs, HttpStatus.OK);
	}

	@GetMapping("/album/{album}")
	public ResponseEntity<List<DetailSongDto>> getSongsByGenre(@PathVariable String album) {
		List<DetailSongDto> songs = songService.getSongsByAlbum(album);
		return new ResponseEntity<>(songs, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<?> uploadSong(
			@RequestPart(name = "title") String title,
			@RequestPart(name = "artist") String artist,
			@RequestPart(name = "album") String album,
			@RequestPart("thumbnail") MultipartFile thumbnail,
			@RequestPart("file") MultipartFile file
			) throws IOException {
		DetailSongDto detailSongDto = new DetailSongDto(title, artist, album);
		return songService.uploadSong(detailSongDto, thumbnail, file);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> updateSong(
			@PathVariable int id,
			@RequestBody DetailSongDto detailSongDTO
	) {
		return songService.updateSong(id, detailSongDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSong(@PathVariable int id) {
		return songService.deleteSong(id);
	}

	@GetMapping("/suggestions/{keyword}")
	public ResponseEntity<List<String>> getSearchSuggestions(
			@PathVariable String keyword
	) {
		List<String> suggestionsTitle = songRepository.findSuggestionsTitle(keyword);
		List<String> suggestionsArtist = songRepository.findSuggestionsArtist(keyword);
		List<String> suggestionsAlbum = songRepository.findSuggestionsAlbum(keyword);

		List<String> allSuggestions = new ArrayList<>();

		allSuggestions.addAll(suggestionsTitle);
		allSuggestions.addAll(suggestionsArtist);
		allSuggestions.addAll(suggestionsAlbum);

		return ResponseEntity.ok(allSuggestions);
	}

//	@GetMapping("/suggestionstest/{keyword}")
//	public List<Song> searchSongsByTitle(@PathVariable  String keyword) {
//		return songRepository.findByTitleFullTextSearch(keyword);
//	}

	@GetMapping("/{id}/download")
	public ResponseEntity<DetailSongDto> downloadSong(
			@PathVariable int id
	) {
		return songService.downloadSong(id);
	}

	@GetMapping("/{name}/file")
	public ResponseEntity<Resource> downloadFile(@PathVariable String name) {
		return songService.downloadFile(name);
	}

	@GetMapping("/{name}/image")
	public ResponseEntity<Resource> downloadImage(@PathVariable String name) {
		return songService.downloadImage(name);
	}

	@GetMapping("/albums")
	public ResponseEntity<List<String>> getAllAlbums() {
		List<String> albums = songService.getAllAlbums();
		return new ResponseEntity<>(albums, HttpStatus.OK);
	}

	@GetMapping("/albums/{album}/songs")
	public ResponseEntity<List<DetailSongDto>> getSongsFromAlbum(@PathVariable String album) {
		List<DetailSongDto> songDtos = songService.getSongsFromAlbum(album);
		return new ResponseEntity<>(songDtos, HttpStatus.OK);
	}
}