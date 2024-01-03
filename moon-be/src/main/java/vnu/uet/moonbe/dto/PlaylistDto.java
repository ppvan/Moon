package vnu.uet.moonbe.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDto {

	private int id;
	private String name;
	private String userName;

	public PlaylistDto(String name) {
		this.name = name;
	}
}
