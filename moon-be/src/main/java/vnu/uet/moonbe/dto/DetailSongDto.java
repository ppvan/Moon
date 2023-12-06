package vnu.uet.moonbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailSongDto {

  private int  id;
  private String title;
  private String artist;
  private String genre;
  private String filePath;

	public DetailSongDto(String title, String artist, String genre) {
		this.title = title;
		this.artist = artist;
		this.genre = genre;
	}
}
