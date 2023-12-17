package vnu.uet.moonbe.other;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUploader {

	public static void main(String[] args) {
//		String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDI1MDczNTAsImV4cCI6MTcwMjU5Mzc1MH0.eSmDIUjWEqNE45uCFTYaJ2tiJ31d7mldF7Cy02rTJNw";
		String mp3FolderPath = "D:\\Downloads\\seed-data\\music";
		String imageFolderPath = "D:\\Downloads\\seed-data\\thumbnail";

		List<File> mp3Files = getFiles(mp3FolderPath, ".mp3");

		RestTemplate restTemplate = new RestTemplate();

		String apiEndpoint = "http://139.59.227.169:8080/api/v1/songs/upload";
//		String apiEndpoint = "http://localhost:8080/api/v1/songs/upload";

		for (File mp3File : mp3Files) {
			try {
				DetailSongDto detailSongDto = extractMp3Info(mp3File);

				String imageFileName = mp3File.getName().replace(".mp3", ".png");

				File imageFile = new File(imageFolderPath, imageFileName);

				MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
				formData.add("title", detailSongDto.getTitle());
				formData.add("artist", detailSongDto.getArtist());
				formData.add("album", detailSongDto.getAlbum());
				formData.add("thumbnail", new FileSystemResource(imageFile));
				formData.add("file", new FileSystemResource(mp3File));

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//				headers.set("Authorization", "Bearer " + token);

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

			} catch (Exception e) {
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

	private static DetailSongDto extractMp3Info(File mp3File) throws Exception {
		AudioFile audioFile = AudioFileIO.read(mp3File);
		Tag tag = audioFile.getTag();

		String title = "", artist = "", album = "";

		title = tag.getFirst(FieldKey.TITLE);
		artist = tag.getFirst(FieldKey.ARTIST);
		album = tag.getFirst(FieldKey.ALBUM);

		System.out.println("Title: " + title);
		System.out.println("Artist: " + artist);
		System.out.println("Album: " + album);

		if (album == "") album = "none";

		return new DetailSongDto(title, artist, album);
	}
}