package mhl.gochariot.repository;

import mhl.gochariot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);

    Optional<Role> findById(Integer id);
}
