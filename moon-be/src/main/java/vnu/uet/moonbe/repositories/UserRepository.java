package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vnu.uet.moonbe.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

}
