package vnu.uet.moonbe.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song {

  public static final String PLAYLIST_DELIM = ";";

    /*
        This is metadata on the song, we don't store on the server database, it's in the metadata of the file.
        var title string
        var artist string
        var album string
        var genre string
        var year string
        var comment string
        var thubnail string
    */

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  //    title is the name of the file on local side.
  private String title;

  private String artist;
  private String genre;

  //    Hashed is the hash of the file, in this system we consider same hash = same file.
  //private String hashed;
//    The song file has to be on somewhere on server, this is the path.
  private String filePath;

  //    @ManyToMany(mappedBy = "songs")
//    private List<User> users = new ArrayList<>();

//  @OneToMany(mappedBy = "song")
//  private List<UserSongMapping> userSongMappings = new ArrayList<>();

//    A semi-colon separate list of playlist: Default;Favourite;....
  //private String playlists;

//  public Song(int id, String title, String artist, String genre, String filePath) {
//    this.id = id;
//    this.title = title;
//    this.artist = artist;
//    this.genre = genre;
//    this.filePath = filePath;
//  }

//  public void addUser(User user, String typeService) {
//    UserSongMapping userSongMapping = new UserSongMapping();
//    userSongMapping.setUser(user);
//    userSongMapping.setTypeService(typeService);
//
//    user.getUserSongMappings().add(userSongMapping);
//    userSongMapping.setSong(this);
//
////        users.add(user);
////        user.getSongs().add(this);
//  }

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
}
