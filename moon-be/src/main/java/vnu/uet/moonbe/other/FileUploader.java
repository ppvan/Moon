package vnu.uet.moonbe.other;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vnu.uet.moonbe.dto.DetailSongDto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileUploader {

	public static void main(String[] args) {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDI1MDczNTAsImV4cCI6MTcwMjU5Mzc1MH0.eSmDIUjWEqNE45uCFTYaJ2tiJ31d7mldF7Cy02rTJNw";
		String mp3FolderPath = "D:\\Repo\\file";
		String jsonFolderPath = "D:\\Repo\\json";

		List<File> mp3Files = getFiles(mp3FolderPath, ".mp3");
		List<File> jsonFiles = getFiles(jsonFolderPath, ".info.json");

		RestTemplate restTemplate = new RestTemplate();

		String apiEndpoint = "http://139.59.227.169:8080/api/v1/songs/upload";
//		String apiEndpoint = "http://localhost:8080/api/v1/songs/upload";

		for (File mp3File : mp3Files) {
			File jsonFile = findJsonFile(mp3File, jsonFiles);

			if (jsonFile == null) {
				System.out.println("No corresponding json file found");
				continue;
			}

			try {
				String jsonContent = new String(Files.readAllBytes(jsonFile.toPath()));

				DetailSongDto detailSongDto = extractInfoFromJson(jsonContent);

				MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

				formData.add("title", detailSongDto.getTitle());
				formData.add("artist", detailSongDto.getArtist());
				formData.add("album", detailSongDto.getAlbum());
				formData.add("file", new FileSystemResource(mp3File));

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				headers.set("Authorization", "Bearer " + token);

				HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiEndpoint);

				ResponseEntity<String> responseEntity = restTemplate.postForEntity(
						builder.build().toUriString(),
						requestEntity,
						String.class
				);

				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					System.out.println("Uploaded successfully: " + mp3File.getName());
				} else {
					System.out.println("Upload failed: " + mp3File.getName());
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<File> getFiles(String folderPath, String extension) {
		List<File> files = new ArrayList<>();
		File folder = new File(folderPath);

		if (folder.exists() && folder.isDirectory()) {
			File[] fileList = folder.listFiles(((dir, name) -> name.endsWith(extension)));
			if (fileList != null) {
				files.addAll(List.of(fileList));
			}
		}

		return files;
	}

	private static File findJsonFile(File mp3File, List<File> jsonFiles) {
		String mp3FileName = mp3File.getName();
		String jsonFileName = mp3FileName.replace(".mp3", ".info.json");

		return jsonFiles.stream()
				.filter(file -> file.getName().equalsIgnoreCase(jsonFileName))
				.findFirst()
				.orElse(null);
	}

	private static DetailSongDto extractInfoFromJson(String jsonContent) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(jsonContent);

		String title, artist, album;

		if (jsonNode.has("title")) {
			title = jsonNode.path("title").asText();
		} else {
			title = "empty";
		}

		if (jsonNode.has("artist")) {
			artist = jsonNode.path("artist").asText();
		} else if (jsonNode.has("channel")) {
			artist = jsonNode.path("channel").asText();
		} else {
			artist = "empty";
		}

		if (jsonNode.has("album")) {
			album = jsonNode.path("album").asText();
		} else {
			album = "empty";
		}

		System.out.println("Title: " + title);
		System.out.println("Artist: " + artist);
		System.out.println("Album: " + album);

		return new DetailSongDto(title, artist, album);
	}
}