package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnu.uet.moonbe.models.Playlist;
import vnu.uet.moonbe.models.PlaylistItem;
import vnu.uet.moonbe.models.Song;

import java.util.List;

@Repository
public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Integer> {

	List<PlaylistItem> findByPlaylist(Playlist playlist);

	PlaylistItem findBySong(Song song);

	PlaylistItem findByPlaylistAndSong(Playlist playlist, Song song);
}
