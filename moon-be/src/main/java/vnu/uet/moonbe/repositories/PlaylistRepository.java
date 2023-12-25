package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnu.uet.moonbe.models.Playlist;
import vnu.uet.moonbe.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

	Optional<Playlist> findById(int id);

	List<Playlist> findByUser(User user);
}
