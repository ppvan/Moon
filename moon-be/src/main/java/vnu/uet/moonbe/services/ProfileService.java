package vnu.uet.moonbe.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vnu.uet.moonbe.dto.ProfileDto;
import vnu.uet.moonbe.dto.ResponseDto;
import vnu.uet.moonbe.models.User;
import vnu.uet.moonbe.repositories.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

	@Value("${avatar.upload.path}")
	private String avatarUploadPath;

	private final UserRepository userRepository;

	private String urlApiAvatar = "http://139.59.227.169:8080/api/profile/avatar/";

	public ResponseEntity<ProfileDto> getUserProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String userEmail = authentication.getName();

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		ProfileDto profileDto = new ProfileDto();
		profileDto.setEmail(user.getEmail());
		profileDto.setFirstname(user.getFirstname());
		profileDto.setLastname(user.getLastname());
		profileDto.setAvatar(user.getAvatar());

		return ResponseEntity.ok(profileDto);
	}

	public ResponseEntity<?> updateUserProfile(ProfileDto profileDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String userEmail = authentication.getName();

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		user.setFirstname(profileDto.getFirstname());
		user.setLastname(profileDto.getLastname());

		userRepository.save(user);

		return ResponseEntity.ok("Profile updated successfully");
	}

	public ResponseEntity<?> updateAvatar(MultipartFile file) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}

		String uuid = UUID.randomUUID().toString();

		String newFileName = uuid + ".png";
		Path newFilePath = Paths.get(avatarUploadPath, newFileName);
		try {
			Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
		}

		User user = optionalUser.get();
		user.setAvatar(urlApiAvatar + newFileName);
		userRepository.save(user);

		ResponseDto responseDto = new ResponseDto();
		responseDto.setStatusCode(HttpStatus.CREATED.value());
		responseDto.setMessage("Avatar updated success");

		return ResponseEntity.ok(responseDto);
	}

	public ResponseEntity<Resource> downloadAvatar(String name) {
		String filePath = avatarUploadPath + File.separator + name;

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
}
