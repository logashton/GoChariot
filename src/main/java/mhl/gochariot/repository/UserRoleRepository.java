package mhl.gochariot.repository;

import mhl.gochariot.model.UserRole;
import mhl.gochariot.model.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {

}