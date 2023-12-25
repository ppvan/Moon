package vnu.uet.moonbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "playlist_song")
public class PlaylistItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	@JoinColumn(name = "playlist_id", referencedColumnName = "id")
	private Playlist playlist;

	@ManyToOne
	@JoinColumn(name = "song_id", referencedColumnName = "id")
	private Song song;

	public PlaylistItem(Playlist playlist, Song song) {
		this.playlist = playlist;
		this.song = song;
	}
}
