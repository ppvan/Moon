package vnu.uet.moonbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song {

//  public static final String PLAYLIST_DELIM = ";";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String title;
  private String artist;
  private String genre;

	@Column(length = 255)
  private String filePath;

  @OneToMany(mappedBy = "song")
  private List<UserSongMapping> userSongMappings = new ArrayList<>();

  public Song(int id, String title, String artist, String genre, String filePath) {
    this.id = id;
    this.title = title;
    this.artist = artist;
    this.genre = genre;
    this.filePath = filePath;
  }
}

//    A semi-colon separate list of playlist: Default;Favourite;....
//private String playlists;

//    public Song() {
//        this("", "", "");
//    }
//
//    public Song(String title, String hashed) {
//        this(title, hashed, "", "");
//    }
//
//    public Song(String title, String hashed, String playlists) {
//        this(title, hashed, playlists, "");
//    }
//
//    public Song(String title, String hashed, String playlists, String filePath) {
//        this.title = title;
//        this.hashed = hashed;
//        this.playlists = playlists;
//        this.filePath = filePath;
//    }

//    public void addPlaylist(String playlist) {
//        String[] playlistTmp = this.playlists.split(PLAYLIST_DELIM);
//        List<String> newPlaylist = new ArrayList<>(List.of(playlistTmp));
//        newPlaylist.add(playlist);
//
//        this.playlists = String.join(PLAYLIST_DELIM, newPlaylist);
//    }