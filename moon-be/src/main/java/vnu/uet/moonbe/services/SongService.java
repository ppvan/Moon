package vnu.uet.moonbe.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vnu.uet.moonbe.dto.DetailSongDto;
import vnu.uet.moonbe.dto.ResponseDto;
import vnu.uet.moonbe.exceptions.SongNotFoundException;
import vnu.uet.moonbe.models.Song;
import vnu.uet.moonbe.repositories.SongRepository;
import vnu.uet.moonbe.repositories.UserActionRepository;
import vnu.uet.moonbe.repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongService {

	@Value("${url.base.path}")
	private String urlBasePath;

	@Value("${file.upload.path}")
	private String fileUploadPath;

	@Value("${image.upload.path}")
	private String imageUploadPath;

	private String urlApiSong = "http://139.59.227.169:8080/api/songs/";

	private final UserRepository userRepository;
	private final SongRepository songRepository;
	private final UserActionRepository userActionRepository;

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

	public List<DetailSongDto> getSongsByAlbum(String keyword) {
		List<Song> songs = songRepository.findByAlbumContainingIgnoreCase(keyword);
		return songs.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
	}

	public DetailSongDto getSongById(int id) {
		Song song = songRepository.findById(id)
				.orElseThrow(() -> new SongNotFoundException("Song could not be found"));

		return mapToDto(song);
	}

	public ResponseEntity<?> uploadSong(DetailSongDto detailSongDTO, MultipartFile thumbnail, MultipartFile file) throws IOException {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String email = authentication.getName();
//
//		Optional<User> optionalUser = userRepository.findByEmail(email);
//		if (optionalUser.isEmpty()) {
//			throw new UsernameNotFoundException("User not found");
//		}

		if (detailSongDTO == null) {
			throw new SongNotFoundException("No song details found");
		}

//		if (StringUtils.isBlank(detailSongDTO.getTitle()) ||
//				StringUtils.isBlank(detailSongDTO.getArtist())
//		) {
//			throw new SongNotFoundException("No song details found");
//		}

		String uuid = UUID.randomUUID().toString();

		String newFileName = uuid + ".mp3";
		Path newFilePath = Paths.get(fileUploadPath, newFileName);
//		String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
//		String filePath = fileUploadPath + File.separator + fileName;
		try {
			Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
		}

		String newImageName = uuid + ".png";
		Path newImagePath = Paths.get(imageUploadPath, newImageName);
//		String imageName = org.springframework.util.StringUtils.cleanPath(thumbnail.getOriginalFilename());
//		String imagePath = imageUploadPath + File.separator + imageName;
		try {
			Files.copy(thumbnail.getInputStream(), newImagePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
		}

		Song newSong = new Song();
		newSong.setTitle(detailSongDTO.getTitle());
		newSong.setArtist(detailSongDTO.getArtist());
		newSong.setAlbum(detailSongDTO.getAlbum());
		newSong.setThumbnail(urlApiSong + newImageName + "/image");
		newSong.setFilePath(urlApiSong + newFileName + "/file");

		songRepository.save(newSong);

//		User user = optionalUser.get();
//		user.addSong(newSong, ActionType.UPLOAD);
//		userRepository.save(user);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatusCode(HttpStatus.CREATED.value());
		responseDto.setMessage("Song added success");

		return ResponseEntity.ok(responseDto);
	}

	public ResponseEntity<Resource> downloadFile(String name) {
		String filePath = fileUploadPath + File.separator + name;

		File file = new File(filePath);
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}

		Resource resource = new FileSystemResource(file);

		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentType(MediaType.parseMediaType("audio/mpeg")); // Đặt kiểu MIME của file mp3
		headers.setContentDisposition(ContentDisposition.builder("inline").filename(name).build()); // Thiết lập để mở file trực tiếp

		return ResponseEntity.ok()
				.headers(headers)
				.body(resource);
	}

	public ResponseEntity<Resource> downloadImage(String name) {
		String filePath = imageUploadPath + File.separator + name;

		File file = new File(filePath);
		if (!file.exists()) {
			return ResponseEntity.notFound().build();
		}

		Resource resource = new FileSystemResource(file);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentDisposition(ContentDisposition.builder("inline").filename(name).build());

		return ResponseEntity.ok()
				.headers(headers)
				.body(resource);
	}

	public ResponseEntity<DetailSongDto> downloadSong(int id) {
		Song song = songRepository.findById(id)
				.orElseThrow(() -> new SongNotFoundException("Song could not be found"));

		DetailSongDto songDto = mapToDto(song);
		songDto.setFilePath(urlBasePath + songDto.getFilePath());

		return ResponseEntity.ok(songDto);
	}

	public ResponseEntity<?> updateSong(int id, DetailSongDto detailSongDTO) {
		Song song = songRepository.findById(id)
				.orElseThrow(() -> new SongNotFoundException("Song could not be updated"));

		song.setTitle(detailSongDTO.getTitle());
		song.setArtist(detailSongDTO.getArtist());
		song.setAlbum(detailSongDTO.getAlbum());

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

		if (!song.getUserActions().isEmpty()) {
			userActionRepository.deleteAll(song.getUserActions());
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
		detailSongDTO.setAlbum(song.getAlbum());
		detailSongDTO.setThumbnail(song.getThumbnail());
		detailSongDTO.setFilePath(song.getFilePath());

		return detailSongDTO;
	}

	private Song mapToEntity(DetailSongDto detailSongDTO) {
		Song song = new Song();

		song.setId(detailSongDTO.getId());
		song.setTitle(detailSongDTO.getTitle());
		song.setArtist(detailSongDTO.getArtist());
		song.setAlbum(detailSongDTO.getAlbum());
		song.setThumbnail(detailSongDTO.getThumbnail());
		song.setFilePath(detailSongDTO.getFilePath());

		return song;
	}
}