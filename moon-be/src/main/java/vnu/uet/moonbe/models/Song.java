package vnu.uet.moonbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Song {

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
    private Long id;

//    title is the name of the file on local side.
    private String title;
//    Hashed is the hash of the file, in this system we consider same hash = same file.
    private String hashed;
//    The song file has to be on somewhere on server, this is the path.
    private String filePath;

//    A semi-colon separate list of playlist: Default;Favourite;....
    private String playlists;
}
