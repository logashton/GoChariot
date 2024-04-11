package mhl.gochariot.repository;

import mhl.gochariot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByUserId(int userId);
    Optional<User> findByEmail(String email);
}
