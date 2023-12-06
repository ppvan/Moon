package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vnu.uet.moonbe.models.UserSongMapping;

public interface UserSongMappingRepository extends JpaRepository<UserSongMapping, Integer> {

}
