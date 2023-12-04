package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnu.uet.moonbe.models.Song;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

  Optional<Song> findById(int id);

  List<Song> findByTitleContainingIgnoreCase(String keyword);

  List<Song> findByArtistContainingIgnoreCase(String keyword);

  List<Song> findByGenreContainingIgnoreCase(String keyword);


  //Boolean exitsByTitle(String title);

  //Optional<Song> findByTitle(String title);
}
