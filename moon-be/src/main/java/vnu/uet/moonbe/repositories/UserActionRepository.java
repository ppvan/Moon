package vnu.uet.moonbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnu.uet.moonbe.models.UserAction;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Integer> {

}
