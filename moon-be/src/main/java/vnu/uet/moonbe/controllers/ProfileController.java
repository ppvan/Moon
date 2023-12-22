package vnu.uet.moonbe.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vnu.uet.moonbe.dto.ProfileDto;
import vnu.uet.moonbe.services.ProfileService;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping("/")
	public ResponseEntity<ProfileDto> profile() {
		return profileService.getUserProfile();
	}

	@PatchMapping("/")
	public ResponseEntity<?> updateUserProfile(
			@RequestBody ProfileDto profileDto
	) {
		return profileService.updateUserProfile(profileDto);
	}

	@PatchMapping("/avatar")
	public ResponseEntity<?> updateAvatar(
			@RequestPart("avatar") MultipartFile file
			) {
		return profileService.updateAvatar(file);
	}

	@GetMapping("/avatar/{name}")
	public  ResponseEntity<Resource> downloadAvatar(@PathVariable String name) {
		return profileService.downloadAvatar(name);
	}
}
