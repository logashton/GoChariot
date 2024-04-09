package mhl.gochariot.repository;

import mhl.gochariot.model.User;
import mhl.gochariot.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUser(User user);
}