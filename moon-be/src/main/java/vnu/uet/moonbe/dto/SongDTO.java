package vnu.uet.moonbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {

  private int  id;
  private String title;
  private String artist;
  private String genre;
  private String filePath;
}
