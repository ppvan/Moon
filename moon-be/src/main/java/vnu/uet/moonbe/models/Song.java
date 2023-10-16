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
    private String fileName;

    @ManyToOne
    private Playlist playlist;
}
