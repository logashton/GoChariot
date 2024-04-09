package mhl.gochariot.repository;

import mhl.gochariot.model.User;
import mhl.gochariot.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUser(User user);
}
