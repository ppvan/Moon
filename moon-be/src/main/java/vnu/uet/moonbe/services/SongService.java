package vnu.uet.moonbe.services;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vnu.uet.moonbe.dto.DetailSongDto;
import vnu.uet.moonbe.dto.ResponseDto;
import vnu.uet.moonbe.exceptions.SongNotFoundException;
import vnu.uet.moonbe.models.ActionType;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.models.User;
import vnu.uet.moonbe.repositories.SongRepository;
import vnu.uet.moonbe.repositories.UserRepository;
import vnu.uet.moonbe.repositories.UserSongMappingRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

	@Value("${file.upload.path}")
	private String uploadPath;

	private final UserRepository userRepository;
	private final SongRepository songRepository;
	private final UserSongMappingRepository userSongMappingRepository;

	public List<DetailSongDto> getAllSongs() {
		List<Song> songs = songRepository.findAll();
		return songs.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public List<DetailSongDto> getSongsByTitle(String keyword) {
		List<Song> songs = songRepository.findByTitleContainingIgnoreCase(keyword);
		return songs.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public List<DetailSongDto> getSongsByArtist(String keyword) {
		List<Song> songs = songRepository.findByArtistContainingIgnoreCase(keyword);
		return songs.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public List<DetailSongDto> getSongsByGenre(String keyword) {
		List<Song> songs = songRepository.findByGenreContainingIgnoreCase(keyword);
		return songs.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public DetailSongDto getSongById(int id) {
		Song song = songRepository.findById(id)
				.orElseThrow(() -> new SongNotFoundException("Song could not be found"));

		return mapToDto(song);
	}

	public ResponseEntity<?> uploadSong(DetailSongDto detailSongDTO, MultipartFile file) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}

		if (detailSongDTO == null) {
			throw new SongNotFoundException("No song details found");
		}

		if (StringUtils.isBlank(detailSongDTO.getTitle()) ||
				StringUtils.isBlank(detailSongDTO.getArtist()) ||
				StringUtils.isBlank(detailSongDTO.getGenre())
		) {
			throw new SongNotFoundException("No song details found");
		}

		String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		String filePath = uploadPath + File.separator + fileName;
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
		}

		Song newSong = new Song();
		newSong.setTitle(detailSongDTO.getTitle());
		newSong.setArtist(detailSongDTO.getArtist());
		newSong.setGenre(detailSongDTO.getGenre());
		newSong.setFilePath(filePath);

		songRepository.save(newSong);

		User user = optionalUser.get();
		user.addSong(newSong, ActionType.UPLOAD);
		userRepository.save(user);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatusCode(HttpStatus.CREATED.value());
		responseDto.setMessage("Song added success");

		return ResponseEntity.ok(responseDto);
	}

	public ResponseEntity<FileSystemResource> downloadSong(int id) {
		Song song = songRepository.findById(id)
				.orElseThrow(() -> new SongNotFoundException("Song could not be found"));

		File songFile = new File(song.getFilePath());
		FileSystemResource fileSystemResource = new FileSystemResource(songFile);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + songFile.getName());
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(songFile.length()));

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(songFile.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(fileSystemResource);
	}

	public ResponseEntity<?> updateSong(int id, DetailSongDto detailSongDTO) {
		Song song = songRepository.findById(id)
				.orElseThrow(() -> new SongNotFoundException("Song could not be updated"));

		song.setTitle(detailSongDTO.getTitle());
		song.setArtist(detailSongDTO.getArtist());
		song.setGenre(detailSongDTO.getArtist());
		song.setFilePath(detailSongDTO.getFilePath());

		songRepository.save(song);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatusCode(HttpStatus.OK.value());
		responseDto.setMessage("Song updated success");

		return ResponseEntity.ok(responseDto);
	}

	@Transactional
	public ResponseEntity<?> deleteSong(int id) {
		Song song = songRepository.findById(id)
						.orElseThrow(() -> new SongNotFoundException("Song could not be deleted"));

		if (!song.getUserSongMappings().isEmpty()) {
			userSongMappingRepository.deleteAll(song.getUserSongMappings());
		}

		songRepository.delete(song);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatusCode(HttpStatus.NO_CONTENT.value());
		responseDto.setMessage("Song deleted success");

		return ResponseEntity.ok(responseDto);
	}

	private DetailSongDto mapToDto(Song song) {
		DetailSongDto detailSongDTO = new DetailSongDto();

		detailSongDTO.setId(song.getId());
		detailSongDTO.setTitle(song.getTitle());
		detailSongDTO.setArtist(song.getArtist());
		detailSongDTO.setGenre(song.getGenre());
		detailSongDTO.setFilePath(song.getFilePath());

		return detailSongDTO;
	}

	private Song mapToEntity(DetailSongDto detailSongDTO) {
		Song song = new Song();

		song.setId(detailSongDTO.getId());
		song.setTitle(detailSongDTO.getTitle());
		song.setArtist(detailSongDTO.getArtist());
		song.setGenre(detailSongDTO.getGenre());
		song.setFilePath(detailSongDTO.getFilePath());

		return song;
	}
}