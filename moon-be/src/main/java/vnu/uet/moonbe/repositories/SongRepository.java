package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vnu.uet.moonbe.models.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
}
