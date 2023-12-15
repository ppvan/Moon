package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vnu.uet.moonbe.models.Song;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

  Optional<Song> findById(int id);

  List<Song> findByTitleContainingIgnoreCase(String keyword);

  List<Song> findByArtistContainingIgnoreCase(String keyword);

  List<Song> findByAlbumContainingIgnoreCase(String keyword);

	@Query("SELECT DISTINCT s.title FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<String> findSuggestionsTitle(@Param("keyword") String keyword);

	@Query("SELECT DISTINCT s.artist FROM Song s WHERE LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<String> findSuggestionsArtist(@Param("keyword") String keyword);

	@Query("SELECT DISTINCT s.album FROM Song s WHERE LOWER(s.album) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<String> findSuggestionsAlbum(@Param("keyword") String keyword);
}
