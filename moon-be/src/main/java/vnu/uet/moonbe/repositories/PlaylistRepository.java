package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnu.uet.moonbe.models.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
